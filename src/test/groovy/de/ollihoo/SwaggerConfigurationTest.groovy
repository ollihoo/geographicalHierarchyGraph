package de.ollihoo

import com.fasterxml.classmate.TypeResolver
import spock.lang.Specification

class SwaggerConfigurationTest extends Specification {

    private SwaggerConfiguration swaggerConfiguration

    TypeResolver typeResolverMock

    def setup() {
        typeResolverMock = Mock(TypeResolver)
        swaggerConfiguration = new SwaggerConfiguration(typeResolver: typeResolverMock)
    }

    def "swagger configuration contains docket definition" () {
        when:
        def api = swaggerConfiguration.getApiDefinition()

        then:
        api != null

    }


}
