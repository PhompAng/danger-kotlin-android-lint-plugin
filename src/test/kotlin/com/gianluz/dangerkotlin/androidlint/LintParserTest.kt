package com.gianluz.dangerkotlin.androidlint

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class LintParserTest {

    @Test
    fun execute() {
        val issues = LintParser.parse(getFilePathFromResources())
        assertEquals(issues.version, "lint 3.4.0")
        assertEquals(issues.issues.size, 3)
        with(issues.issues[0]) {
            assertEquals(id, "OldTargetApi")
            assertEquals(severity, "Warning")
            assertEquals(message.contains("Not targeting the latest version"), true)
            assertEquals(category, "Correctness")
            assertEquals(priority, "6")
            assertEquals(summary.contains("Target SDK"), true)
            assertEquals(explanation.contains("When your application runs on a version"), true)
        } // etc.. TODO: write all assertions
    }

    @Test
    fun parseLintResultsWithMissingAttributes() {
        val issues = LintParser.parse(getFilePathFromResources("lint-results-missing-attributes.xml"))
        assertEquals("lint 4.1.0", issues.version)
        assertEquals(3, issues.issues.size)

        with(issues.issues[0]) {
            assertEquals("LintError", id)
            assertEquals("Error", severity)
            assertEquals("/src/main/AndroidManifest.xml", location.file)
            assertEquals("", location.line)
            assertEquals("", location.column)
        }

        with(issues.issues[1]) {
            assertEquals("OldTargetApi", id)
            assertEquals("12", location.line)
            assertEquals("9", location.column)
        }

        with(issues.issues[2]) {
            assertEquals("UnusedResources", id)
            assertEquals("", location.file)
            assertEquals("", location.line)
            assertEquals("", location.column)
        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getFilePathFromResources(name: String = "lint-results.xml"): String {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(name).file)
        return file.absolutePath
    }
}
