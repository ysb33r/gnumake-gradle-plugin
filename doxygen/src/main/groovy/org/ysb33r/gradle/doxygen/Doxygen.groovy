// ============================================================================
// (C) Copyright Schalk W. Cronje 2014
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================

package org.ysb33r.gradle.doxygen


import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.process.ExecResult
import org.ysb33r.gradle.doxygen.impl.DoxyfileEditor
import org.ysb33r.gradle.doxygen.impl.DoxygenProperties
import org.ysb33r.gradle.doxygen.impl.Executables

/**
 * A wrapper task for calling GNU Make. This is useful for migrating legacy builds
 * or when complex build need to construct components that use GNU Make as a
 * build tool.
 *
 * @author Schalk W. Cronjé
 */
class Doxygen extends SourceTask {

    private def paths = [doxygen: 'doxygen']
    private def templateFile = null
    private DoxygenProperties doxyUpdate = new DoxygenProperties()
    private List<File> imagePaths = []

    @Optional
    @OutputDirectory
    File outputDir = new File(project.buildDir, 'docs/doxygen')

    /** Constructs a Doxygen task object and sets some default Doxygen properties
     *
     */
    Doxygen() {

        doxyUpdate.setProperty('PROJECT_NUMBER', project.version)
        doxyUpdate.setProperty('PROJECT_NAME', project.name)
        doxyUpdate.setProperty('QUIET', project.logger.isQuietEnabled())
        doxyUpdate.setProperty('WARNINGS', !project.logger.isQuietEnabled())

    }

//    /** Indicates whether Xml should be generated
//     *
//     * @param genXml
//     */
//    def xml(boolean genXml) {
//    }
//
//    /** Allows for the configuration of the XML block
//     *
//     * @param cfg
//     */
//    def xml(Closure cfg) {
//    }

    /** Returns a map containing all of the executable paths that have been set
     *
     * @return
     */
     def getExecutables() { paths.asImmutable() }

    /** Allows for setting paths to various executables. By default they would be searched for
     * in the search path.
     *
     * @param cfg A configuration closure
     *
     * <li>doxygen
     * <li>mscgen
     */
    void executables(Closure cfg) {
        Closure cl = cfg.clone()
        cl.delegate = new Executables(paths)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    /** Sets the template Doxyfile to be used. If not supplied a default one will be
     * generated to be used as a template.
     *
     * @param tmpl Template Doxyfile
     */
    void template(final File tmpl) {
        inputs.file tmpl
        templateFile = tmpl
    }

    /** Sets the template Doxyfile to be used. If not supplied a default one will be
     * generated to be used as a template.
     *
     * @param tmpl Template Doxyfile
     */
    void template(final String tmpl) {
        template new File(tmpl)
    }

    /** Allows for one more image paths to be set
     *
     * @param paths
     */
    void image_path(final Object... paths) {

        def workaround = imagePaths

        paths.each {
            File fName
            switch (it) {
                case File:
                    fName = it
                    break
                default:
                    fName = new File(it.toString())
            }

            if (fName.isDirectory()) {
                inputs.dir fName
            } else {
                inputs.file fName
            }

            workaround.add(fName)
        }
    }

    /** Alias for outputDir
     *
     * @param outdir
     */
    void output_directory(def outdir) {
        outputDir = outdir
    }


    @TaskAction
    void exec() {
        setDefaults()
        File doxyfile = createDoxyfile()
        project.logger.debug "Using ${doxyfile} as Doxygen configuration file"
        editDoxyfile(doxyfile)
        runDoxygen(doxyfile)
    }

    def methodMissing(String name, args) {

        switch (name) {
            case 'quiet':
            case 'warnings':
            case 'subgrouping':
            case 'recursive':
                doxyUpdate.setProperty(name, args[0])
                break

            case 'input':
                throw new DoxygenException("'${name}' is ignored, use 'source' and 'sourceDir' instead (with exclude patterns as appropriate).")
                break

            case 'mscgen_path':
            case 'dot_path':
            case 'perl_path':
            case 'hhc_location':
                throw new DoxygenException("'${name}' is ignored, use 'executables' closure instead.")
                break

            default:

                if (name.find(/.+_.+/) && name.matches(/[_\p{Digit}\p{Lower}]{3,}/)) {
                    if (args.size() == 1) {
                        doxyUpdate.setProperty(name, args[0])
                    } else {
                        doxyUpdate.setProperty(name, args)
                    }
                } else {
                    throw new DoxygenException("'${name}' is not a valid configuration option")
                }
        }

        true
    }

    /** Returns the current hashmap of Doxygen properties that will override settings in the Doxygen file
     */
    def getDoxygenProperties() {
        doxyUpdate.properties.asImmutable()
    }

    /** Set some default values in the doxyUpdate
     *
     */
    @groovy.transform.PackageScope
    void setDefaults() {

        if (imagePaths.size()) {
            doxyUpdate.setProperty('IMAGE_PATH', imagePaths as File[])
        }

        if(source.files.empty) {
            source 'src/main/cpp','src/main/headers','src/main/asm','src/main/objectiveC','src/main/objectiveCpp','src/main/c'
        }
        
        doxyUpdate.setProperty('INPUT', source)
        doxyUpdate.setProperty('OUTPUT_DIRECTORY', outputDir)

        def workaround = doxyUpdate
        paths.each { name,path ->
            if( name != 'doxygen' ) {
                workaround.setProperty(Executables.EXECUTABLES[name],new File(path))
            }
        }
    }

    /** Creates a Doxyfile that will eventually be passed to the Doxygen executable.
     * If a template has been set, if will make a copy of that, otherwise it will call
     * Doxygen to generate a default file.
     *
     * @return The Doxyfile File instance.
     */
    private File createDoxyfile() {
        File doxyfile = new File(project.buildDir, "tmp/${project.name}.doxyfile")
        if(!doxyfile.parentFile.exists()) {
            doxyfile.parentFile.mkdirs()
        }
        if (templateFile) {
            if (!templateFile.exists()) {
                throw new DoxygenException("${templateFile} does not exist")
            }
            doxyfile.text = templateFile.text
        } else {
            runDoxygen(doxyfile, ['-g'])
        }

        return doxyfile
    }

    /** Edits a Doxyfile and replaces existign properties with ones passed down via
     * Gradle configuration.
     *
     * @param doxyfile
     */
    private void editDoxyfile(File doxyfile) {
        DoxyfileEditor editor = new DoxyfileEditor(logger: project.logger)
        editor.update(doxygenProperties, doxyfile)
    }

    /** Runs the Doxygen executable
     *
     * @param doxyfile
     * @param cmdargs
     */
    private void runDoxygen(final File doxyfile, def cmdargs = []) {

        cmdargs.add(doxyfile.absolutePath)
        ExecResult execResult = project.exec {

            executable  executables.doxygen
            args        cmdargs
        }
    }
}



