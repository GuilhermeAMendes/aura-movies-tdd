package br.ifsp.demo.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages("br.ifsp.demo")
@IncludeTags("[US-2]")
@SuiteDisplayName("All User Story 2 Scenarios Tests")
public class UserStory2TestSuite {
}
