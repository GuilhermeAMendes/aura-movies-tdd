package br.ifsp.demo.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages("br.ifsp.demo")
@IncludeTags("[US-5]")
@SuiteDisplayName("All User Story 5 Scenarios Tests")
public class UserStory5TestSuite {
}
