plugins {
    alias(libs.plugins.algomate)
    alias(libs.plugins.jagr.gradle)
}

exercise {
    assignmentId.set("p0")
}

submission {
    // ACHTUNG!
    // Setzen Sie im folgenden Bereich Ihre TU-ID (NICHT Ihre Matrikelnummer!), Ihren Nachnamen und Ihren Vornamen
    // in Anführungszeichen (z.B. "ab12cdef" für Ihre TU-ID) ein!
    studentId = null
    firstName = null
    lastName = null

    // Optionally require own tests for mainBuildSubmission task. Default is false
    requireTests = false
    // Optionally require public grader for mainBuildSubmission task. Default is false
    requireGraderPublic = false
}



jagr {
    graders {
        val graderPublic by getting {
            graderName.set("P0-Public")
            rubricProviderName.set("p0.P0_RubricProvider_Public")
        }
    }
}
