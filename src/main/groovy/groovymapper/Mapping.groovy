package groovymapper

import groovy.transform.AnnotationCollector
import groovy.transform.CompileStatic

@CompileStatic(extensions = 'groovymapper.MappingTypeCheckingExtension')
@MappingAst
@AnnotationCollector
@interface Mapping {
}