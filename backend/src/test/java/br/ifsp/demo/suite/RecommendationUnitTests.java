package br.ifsp.demo.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@IncludeTags("UnitTest")
@SelectPackages("br.ifsp.demo.application.service.RecommendationServiceTest")
@SuiteDisplayName("All Unit Test of recommendation service")
public class RecommendationUnitTests {
}
