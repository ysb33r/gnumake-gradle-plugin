package org.ysb33r.gradle.a2x

/**
 * Created by schalkc on 10/04/2014.
 */
class AsciidocFacade {

    static final def backends = [
            'html' : [ cmdline : ['--format=xhtml'] ],
            'slidy2' : [
                    download : 'http://asciidoc-slidy2-backend-plugin.googlecode.com/files/slidy2-v1.0.3.zip',
                    cmdline : ['--format=slideshow','--backend slidy2']
            ],
            'deckjs' : [
                    download : 'https://github.com/downloads/houqp/asciidoc-deckjs/deckjs-1.6.2.zip',
                    cmdline : ['--backend=deckjs']
            ],
            'html5' :
    return ['--format=xhtml']
    case 'docbook' :
    return ['--format=docbook']
    case 'docbook45' :
    return ['--format=docbook']
    case 'docbook5' :
    return ['--format=docbook']
    case 'fopub' :
    return ['--format=pdf']

    ]

    docbook45, xhtml11, html4, html5, slidy, wordpress or latex (the latex backend is experimental). You can also use the backend alias names html (aliased to xhtml11) or docbook (aliased to docbook45). Defaults to html.

    static def backendToCmdlineOptions(String backend) {
        backends[backend] ? backends[backend].cmdline : []
    }
}
