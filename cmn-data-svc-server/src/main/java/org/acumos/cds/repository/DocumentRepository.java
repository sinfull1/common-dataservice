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

package org.acumos.cds.repository;

import org.acumos.cds.domain.MLPDocument;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends CrudRepository<MLPDocument, String> {

	/**
	 * Gets all documents associated with the specified solution revision and access
	 * type
	 * 
	 * This does not accept a pageable parameter because the number of artifacts for
	 * a single revision and access type is expected to be modest.
	 *
	 * @param revisionId
	 *                           solution revision ID
	 * @param accessTypeCode
	 *                           access type code
	 * @return Iterable of MLPDocument
	 */
	@Query(value = "select d from MLPDocument d, MLPSolRevDocMap m " //
			+ " where d.documentId =  m.documentId " //
			+ " and m.revisionId = :revisionId" //
			+ " and m.accessTypeCode = :accessTypeCode")
	Iterable<MLPDocument> findByRevisionAccess(@Param("revisionId") String revisionId,
			@Param("accessTypeCode") String accessTypeCode);

}
