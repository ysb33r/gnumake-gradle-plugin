package org.ysb33r.gradle.doxygen.impl

import groovy.transform.TupleConstructor
import org.gradle.api.logging.Logger
import org.ysb33r.gradle.doxygen.DoxygenException

import java.util.regex.Matcher

/**
 * Created by schalkc on 21/05/2014.
 */
@TupleConstructor
class DoxyfileEditor {

    Logger logger

    /** Scans for property names and replace them with updated values.
     * Removes all comments.
     * Any occurrences of += are removed if the property is updated
     *
     * If a supplied property is not found in the doxyfile it is logged as a warning
     *
     * @param doxyfile File to be edited
     * @param updates Properties to be edited in file
     */
    void update( def updates = [:], final File doxyfile ) {

        if( !updates.size() ) {
            return
        }

        // Read all lines
        def properties = readProperties(doxyfile)
        updates.each { name,value ->
            if(properties[name] == null) {
                logger.warn "Ignoring '${name}' as it has not been found in the Doxyfile template"
            } else {
                properties[name] = value
            }
        }

        doxyfile.withPrintWriter { w ->
            properties.each { name,value ->
                w.println "${name} = ${value}"
            }
        }
    }

    /** Parses file, discards comments & blank lines.
     *
     * @param doxyfile - File to be parsed
     * @return A map of properties
     */
    private def readProperties( final File doxyfile ) {
        def result = [:]
        Integer lineCount
        doxyfile.eachLine { line,count ->
            if( !( line.size() == 0 || line.startsWith('#') || line.matches(/^\s+$/)) ) {
                Matcher matches = line =~ /^\s*([\p{Upper}\p{Digit}_]{3,})\s*(\+?\=)\s*(.*)$/

                if(!matches || matches[0].size() != 4) {
                    throw new DoxygenException("Doxyfile parsing error ${doxyfile.absolutePath}:${count}")
                }

                if(matches[0][2] == '+=' && result[matches[0][1]]) {
                    result[matches[0][1]]+= " ${matches[0][3]}"
                } else {
                    result[matches[0][1]] = matches[0][3]
                }
            }
        }

        return result
    }
}
