package org.ysb33r.gradle.doxygen.impl

/**
 * Created by schalkc on 23/05/2014.
 */
class Executables {

    static final def EXECUTABLES = [
            'doxygen' : '',
            'mscgen'  : 'MSCGEN_PATH',
            'dot'     : 'DOT_PATH',
            'perl'    : 'PERL_PATH',
            'hhc'     : 'HHC_LOCATION'
    ]

    private def mapToUpdate

    Executables( def map ) {
        mapToUpdate = map
    }

    def methodMissing( String name, args ) {

        if( args.size() == 1 && EXECUTABLES[name] != null ) {
            switch (args[0]) {
                case File:
                    return mapToUpdate[name] = args[0].absolutePath
                default:
                    return mapToUpdate[name] = args[0].toString()
            }
        }

        throw new MissingMethodException(name,Executables.class,args)
    }


}
