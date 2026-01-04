package com.warrantybee.api.dto.response;

import java.sql.Timestamp;

/**
 * Represents a business contract associated with a vendor or business entity.
 */
public class BusinessContractResponse {

    /** Public or secured URL pointing to the contract document. */
    private String url;

    /** Contract start date and time, indicating when the contract becomes effective. */
    private Timestamp startDate;

    /** Contract end date and time, indicating when the contract expires or terminates. */
    private Timestamp endDate;

    /** Indicates whether the contract has been signed by all required parties. */
    private Boolean isSigned;

    /** Timestamp indicating when the contract was signed. */
    private Timestamp signedAt;
}
