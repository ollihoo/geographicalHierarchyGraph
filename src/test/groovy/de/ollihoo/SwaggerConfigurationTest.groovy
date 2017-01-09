package de.ollihoo

import com.fasterxml.classmate.TypeResolver
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder
import springfox.documentation.spring.web.plugins.Docket

class SwaggerConfigurationTest extends Specification {

    private SwaggerConfiguration swaggerConfiguration

    private TypeResolver typeResolverMock
    private Docket docketMock

    def setup() {
        typeResolverMock = Mock(TypeResolver)

        docketMock = Spy(Docket)
        DocketFactory docketFactory = Mock(DocketFactory)
        docketFactory.swagger2Docket >> docketMock

        swaggerConfiguration = new SwaggerConfiguration(typeResolver: typeResolverMock, docketFactory: docketFactory)
    }

    def "swagger configuration gets configuration by factory and returns it" () {
        when:
        Docket docket = swaggerConfiguration.getApiDefinition()

        then:
        docket  == docketMock
    }

    def "Configuration only scans for RestControllers" () {
        ApiSelectorBuilder apiSelectorBuilder = Spy(ApiSelectorBuilder, constructorArgs: [docketMock])

        when:
        swaggerConfiguration.getApiDefinition()

        then:
        1 * docketMock.select() >> apiSelectorBuilder
        1 * apiSelectorBuilder.apis(SwaggerConfiguration.RESTCONTROLLER_ANNOTATION)
    }

    def "Configuration accepts any path" () {
        ApiSelectorBuilder apiSelectorBuilder = Spy(ApiSelectorBuilder, constructorArgs: [docketMock])

        when:
        swaggerConfiguration.getApiDefinition()

        then:
        1 * docketMock.select() >> apiSelectorBuilder
        1 * apiSelectorBuilder.paths(SwaggerConfiguration.ANY_PATH)
    }

}
