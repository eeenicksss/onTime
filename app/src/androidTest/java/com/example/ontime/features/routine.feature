Feature: Add a new task to the routine

  Scenario: Add a valid task
    Given I am on the Create Routine screen
    When I enter "Exercise" as task title and "30" as task duration
    Then the task "Exercise" should appear in the task list

  Scenario: Try to add an invalid task with no title
    Given I am on the Create Routine screen
    When I enter "" as task title and "30" as task duration
    Then the task should not be added

  Scenario: Try to add an invalid task with negative duration
    Given I am on the Create Routine screen
    When I enter "Exercise" as task title and "-10" as task duration
    Then the task should not be added

  Scenario: Try to add a task with a duration over the limit
    Given I am on the Create Routine screen
    When I enter "Exercise" as task title and "121" as task duration
    Then the task should not be added

  Scenario: Add a valid task with maximum duration
    Given I am on the Create Routine screen
    When I enter "Exercise" as task title and "120" as task duration
    Then the task "Exercise" should appear in the task list

  Scenario: Try to add an invalid task with zero duration
    Given I am on the Create Routine screen
    When I enter "Exercise" as task title and "0" as task duration
    Then the task should not be added
