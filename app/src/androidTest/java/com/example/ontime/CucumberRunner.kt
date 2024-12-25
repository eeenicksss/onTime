package com.example.ontime

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith


@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["com/example/ontime/features"],
    glue = ["com.example.ontime.steps"]
)
class CucumberTest

