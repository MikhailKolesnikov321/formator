package sfedu.net.formator.domain

import java.time.Instant

class Report(
    val id: ReportId,
    val studentId: UserId,
    var organizationSupervisorId: UserId?,
    var universitySupervisorId: UserId?,
    var status: ReportStatus,
    var title: ReportTitle,
    var period: ReportPeriod,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
) {
    companion object {
        fun create(
            studentId: UserId,
            organizationSupervisorId: UserId? = null,
            universitySupervisorId: UserId? = null,
            status: ReportStatus,
            title: ReportTitle,
            period: ReportPeriod
        ): Report {
            return Report(
                id = ReportId.generate(),
                studentId = studentId,
                organizationSupervisorId = organizationSupervisorId,
                universitySupervisorId = universitySupervisorId,
                status = status,
                title = title,
                period = period
            )
        }
    }
}