Feature: Adding tasks to a routine
  Validate that tasks are added correctly based on input data.

  Scenario: Add a valid task
    Given I am on the "Create Routine" screen
    When I enter task name "Morning Exercise"
    And I enter task duration "30"
    And I press "Add Task"
    Then I should see "Morning Exercise - 30 min" in the task list

  Scenario: Add a task with duration less than or equal to 0
    Given I am on the "Create Routine" screen
    When I enter task name "Invalid Task"
    And I enter task duration "0"
    And I press "Add Task"
    Then I should not see "Invalid Task" in the task list

  Scenario: Add a task with duration greater than or equal to 121
    Given I am on the "Create Routine" screen
    When I enter task name "Too Long Task"
    And I enter task duration "121"
    And I press "Add Task"
    Then I should not see "Too Long Task" in the task list

  Scenario: Add a task with an empty name
    Given I am on the "Create Routine" screen
    When I enter task name ""
    And I enter task duration "30"
    And I press "Add Task"
    Then I should not see any task with duration "30 min" in the task list

  Scenario: Add a task with name consisting of only spaces
    Given I am on the "Create Routine" screen
    When I enter task name "    "
    And I enter task duration "45"
    And I press "Add Task"
    Then I should not see any task with duration "45 min" in the task list
