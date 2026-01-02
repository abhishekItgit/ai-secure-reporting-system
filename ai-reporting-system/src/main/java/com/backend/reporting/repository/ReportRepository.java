package com.backend.reporting.repository;


import com.backend.reporting.dto.ReportResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReportResponse> revenueByRegion() {
        return jdbcTemplate.query(
                "SELECT region, total_revenue FROM vw_revenue_by_region",
                (rs, i) -> new ReportResponse(
                        rs.getString("region"),
                        rs.getBigDecimal("total_revenue")
                )
        );
    }

    public List<ReportResponse> orderCountByStatus() {
        return jdbcTemplate.query(
                "SELECT order_status, total_orders FROM vw_order_count_by_status",
                (rs, i) -> new ReportResponse(
                        rs.getString("order_status"),
                        rs.getInt("total_orders")
                )
        );
    }

    public List<ReportResponse> paymentCountByMode() {
        return jdbcTemplate.query(
                "SELECT payment_mode, total_payments FROM vw_payment_count_by_mode",
                (rs, i) -> new ReportResponse(
                        rs.getString("payment_mode"),
                        rs.getInt("total_payments")
                )
        );
    }
}
