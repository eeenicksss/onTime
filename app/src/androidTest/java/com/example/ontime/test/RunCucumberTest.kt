package com.example.ontime.test

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/androidTest/assets/features"], // Путь к feature-файлам
    glue = ["com.example.ontime.test"],       // Путь к Step Definitions
    monochrome = true
)
class RunCucumberTest