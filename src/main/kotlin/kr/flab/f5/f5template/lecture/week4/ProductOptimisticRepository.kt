package kr.flab.f5.f5template.lecture.week4

import org.springframework.data.jpa.repository.JpaRepository

interface ProductOptimisticRepository: JpaRepository<ProductOptimisticLocking, Long> {
}