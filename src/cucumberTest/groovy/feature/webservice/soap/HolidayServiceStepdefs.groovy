package feature.webservice.soap

import cucumber.api.Scenario

import static cucumber.api.groovy.EN.Given
import static cucumber.api.groovy.EN.Then
import static cucumber.api.groovy.EN.When
import static cucumber.api.groovy.Hooks.Before
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat

/**
 * Created by zhangd on 11/08/2017.
 * holidayServiceGetCountries.feature step definitions
 */

Scenario scenario

Before() { Scenario s ->
    scenario = s
}

Given(~/^I have a SOAP (.+) request of Holiday Service 2$/) { version ->
    initiateHolidayService2SOAPClient()
    soapVersion = version
}

When(~/^I send to the endpoint with (\w+) in SOAP (.+)$/) { name, version ->
    soapResponse = getHolidayService2Response version, name
}

Then(~/^I should get all the countries available$/) { ->
    assertThat soapResponse.httpResponse.statusCode, is(200)
    scenario.write 'SOAP Response:\n' + soapResponse.httpResponse.contentAsString

    def countries = new XmlSlurper().parseText(soapResponse.text).'**'.findAll { node -> node.name() == 'Code' }*.text()
    assertThat countries.size(), is(6)
    assertThat countries, hasItems('Canada', 'Scotland', 'UnitedStates')
}