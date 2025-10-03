package br.ifsp.demo.suite;

import org.junit.platform.suite.api.*;

@Suite
@SelectPackages("br.ifsp.demo")
@IncludeTags("[US-4]")
@SuiteDisplayName("All User Story 4 Scenarios Tests")
public class UserStory4TestSuite {
}
