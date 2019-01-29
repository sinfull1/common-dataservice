/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
 * ===================================================================================
 * This Acumos software file is distributed by AT&T and Tech Mahindra
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END=========================================================
 */

package org.acumos.cds.domain;

import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.acumos.cds.client.ICommonDataServiceRestClient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import io.swagger.annotations.ApiModelProperty;

/**
 * Base model for a solution. Maps all simple columns; maps no complex columns
 * that a subclass might want to map in alternate ways. For example the user
 * column is not mapped here; that is a reference to an MLPUser entity ID, and
 * could be exposed as a string or as an object via Hibernate magic.
 */
@MappedSuperclass
public abstract class MLPAbstractSolution extends MLPTimestampedEntity {

	/* package */ static final String TABLE_NAME = "C_SOLUTION";
	/* package */ static final String USER_ID_COL_NAME = "USER_ID";

	@Id
	@GeneratedValue(generator = "customUseOrGenerate")
	@GenericGenerator(name = "customUseOrGenerate", strategy = "org.acumos.cds.util.UseExistingOrNewUUIDGenerator")
	@Column(name = "SOLUTION_ID", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	// Users MAY submit an ID; readOnly annotation must NOT be used
	@ApiModelProperty(value = "UUID; omit for system-generated value", example = "12345678-abcd-90ab-cdef-1234567890ab")
	// Generated by DB; NotNull annotation not needed
	private String solutionId;

	@Column(name = "NAME", nullable = false, columnDefinition = "VARCHAR(100)")
	@NotNull(message = "Name cannot be null")
	@Size(max = 100)
	@ApiModelProperty(value = "Solution name", example = "My solution")
	private String name;

	@Column(name = "METADATA", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "JSON", example = "{ \"tag\" : \"value\" }")
	private String metadata;

	/**
	 * Inactive means deleted.
	 */
	@Column(name = "ACTIVE_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
	@Type(type = "yes_no")
	@ApiModelProperty(required = true, value = "Boolean indicator")
	private boolean active;

	/**
	 * The valid value set is defined by server-side configuration.
	 */
	@Column(name = "MODEL_TYPE_CD", columnDefinition = "CHAR(2)")
	@Size(max = 2)
	@ApiModelProperty(value = "Model type code", example = "CL")
	private String modelTypeCode;

	/**
	 * The valid value set is defined by server-side configuration.
	 */
	@Column(name = "TOOLKIT_TYPE_CD", columnDefinition = "CHAR(2)")
	@Size(max = 2)
	@ApiModelProperty(value = "Toolkit type code", example = "SK")
	private String toolkitTypeCode;

	/**
	 * Supports federation.
	 */
	@Column(name = "ORIGIN", columnDefinition = "VARCHAR(512)")
	@Size(max = 512)
	@ApiModelProperty(value = "URI of the peer that provided this object")
	private String origin;

	/**
	 * This computed web statistic cannot be updated directly. Updates are blocked
	 * for concurrency safety.
	 */
	@Column(name = "VIEW_COUNT", updatable = false, columnDefinition = "INT")
	@ApiModelProperty(value = "View count", example = "1")
	private Long viewCount = 0L;

	/**
	 * This computed web statistic cannot be updated directly. Updates are blocked
	 * for concurrency safety.
	 */
	@Column(name = "DOWNLOAD_COUNT", updatable = false, columnDefinition = "INT")
	@ApiModelProperty(value = "Download count", example = "1")
	private Long downloadCount = 0L;

	/**
	 * In colDef, don't attempt 'NULL DEFAULT 0'
	 */
	@Deprecated
	@Column(name = "LAST_DOWNLOAD", updatable = false, columnDefinition = "TIMESTAMP")
	@ApiModelProperty(value = "Timestamp of most recent download", example = "2018-12-16T12:34:56.789Z")
	private Instant lastDownload;

	/**
	 * This computed web statistic cannot be updated directly. Updates are blocked
	 * for concurrency safety.
	 */
	@Column(name = "RATING_COUNT", updatable = false, columnDefinition = "INT")
	@ApiModelProperty(value = "Rating count", example = "1")
	private Long ratingCount = 0L;

	/**
	 * This computed web statistic cannot be updated directly. Updates are blocked
	 * for concurrency safety.
	 */
	@Column(name = "RATING_AVG_TENTHS", updatable = false, columnDefinition = "INT")
	@ApiModelProperty(value = "Rating average in tenths; e.g., value 35 means 3.5", example = "35")
	private Long ratingAverageTenths = 0L;

	@Column(name = "FEATURED_YN", columnDefinition = "CHAR(1)")
	@Type(type = "yes_no")
	@ApiModelProperty(value = "Featured indicator")
	private Boolean featured;

	/**
	 * Move web stats fields to solution entity No-arg constructor
	 */
	public MLPAbstractSolution() {
		// no-arg constructor
	}

	/**
	 * This constructor accepts the required fields; i.e., the minimum that the user
	 * must supply to create a valid instance. Omits solution ID, which is generated
	 * on save.
	 * 
	 * @param name
	 *                   Solution Name
	 * @param active
	 *                   Boolean flag
	 */
	public MLPAbstractSolution(String name, boolean active) {
		if (name == null)
			throw new IllegalArgumentException("Null not permitted");
		this.name = name;
		this.active = active;
	}

	/**
	 * Copy constructor
	 * 
	 * @param that
	 *                 Instance to copy
	 */
	public MLPAbstractSolution(MLPAbstractSolution that) {
		super(that);
		this.active = that.active;
		this.downloadCount = that.downloadCount;
		this.featured = that.featured;
		this.lastDownload = that.lastDownload;
		this.metadata = that.metadata;
		this.modelTypeCode = that.modelTypeCode;
		this.name = that.name;
		this.origin = that.origin;
		this.ratingAverageTenths = that.ratingAverageTenths;
		this.ratingCount = that.ratingCount;
		this.solutionId = that.solutionId;
		this.toolkitTypeCode = that.toolkitTypeCode;
		this.viewCount = that.viewCount;
	}

	public String getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String meta) {
		this.metadata = meta;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	/**
	 * @param modelTypeCode
	 *                          A valid model-type code
	 */
	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getToolkitTypeCode() {
		return toolkitTypeCode;
	}

	/**
	 * @param toolkitTypeCode
	 *                            A valid toolkit-type code
	 */
	public void setToolkitTypeCode(String toolkitTypeCode) {
		this.toolkitTypeCode = toolkitTypeCode;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Long getViewCount() {
		return viewCount;
	}

	/**
	 * Clients should NOT call this method because updates are blocked. Instead use
	 * method
	 * {@link ICommonDataServiceRestClient#incrementSolutionViewCount(String)}.
	 * 
	 * @param viewCount
	 *                      count
	 */
	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Long getDownloadCount() {
		return downloadCount;
	}

	/**
	 * Clients should NOT call this method because updates are blocked. This field
	 * is computed from download records created via
	 * {@link ICommonDataServiceRestClient#createSolutionDownload(MLPSolutionDownload)}.
	 * 
	 * 
	 * @param downloadCount
	 *                          count
	 */
	public void setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
	}

	/**
	 * This method will be removed in a future release.
	 * 
	 * @return instant
	 */
	@Deprecated
	public Instant getLastDownload() {
		return lastDownload;
	}

	/**
	 * This method will be removed in a future release.
	 * 
	 * @param lastDownload
	 *                         instant
	 */
	@Deprecated
	public void setLastDownload(Instant lastDownload) {
		this.lastDownload = lastDownload;
	}

	public Long getRatingCount() {
		return ratingCount;
	}

	/**
	 * Clients should NOT call this method because updates are blocked. This field
	 * is computed from rating records created via
	 * {@link ICommonDataServiceRestClient#createSolutionRating(MLPSolutionRating)}.
	 * 
	 * @param ratingCount
	 *                        count
	 */
	public void setRatingCount(Long ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Long getRatingAverageTenths() {
		return ratingAverageTenths;
	}

	/**
	 * Clients should NOT call this method because updates are blocked. This field
	 * is computed from rating records created via
	 * {@link ICommonDataServiceRestClient#createSolutionRating(MLPSolutionRating)}.
	 * 
	 * @param ratingAverageTenths
	 *                                average
	 */
	public void setRatingAverageTenths(Long ratingAverageTenths) {
		this.ratingAverageTenths = ratingAverageTenths;
	}

	public Boolean isFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	/**
	 * The ID field is primary, so defining this method here factors out code.
	 */
	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;
		if (!(that instanceof MLPAbstractSolution))
			return false;
		MLPAbstractSolution thatObj = (MLPAbstractSolution) that;
		return Objects.equals(solutionId, thatObj.solutionId);
	}

	/**
	 * The ID field is primary, so defining this method here factors out code.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(solutionId, name, toolkitTypeCode, modelTypeCode);
	}

}
