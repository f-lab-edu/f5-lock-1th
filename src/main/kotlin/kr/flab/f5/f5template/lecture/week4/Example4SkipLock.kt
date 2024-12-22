package kr.flab.f5.f5template.lecture.week4

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class Example4SkipLock(
    private val jdbcTemplate: JdbcTemplate,
) {

    @Transactional
    fun jdbcTransaction() {
        val stock = jdbcTemplate.queryForObject("select stock from product where id = 1 for update skip locked") { rs, _ ->
            rs.getLong("stock")
        }
    }
}
