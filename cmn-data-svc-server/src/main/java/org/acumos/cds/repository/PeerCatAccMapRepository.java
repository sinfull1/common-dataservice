/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
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

import org.acumos.cds.domain.MLPPeerCatAccMap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PeerCatAccMapRepository extends CrudRepository<MLPPeerCatAccMap, MLPPeerCatAccMap.PeerCatAccMapPK> {

	/**
	 * Gets IDs for catalogs with access specially granted to the peer. These are
	 * expected to be restricted.
	 * 
	 * @param peerId
	 *                   Peer ID
	 * @return Iterable of String
	 */
	@Query(value = "select m.catalogId FROM MLPPeerCatAccMap m " //
			+ " WHERE m.peerId = :peerId")
	Iterable<String> findCatalogIdsByPeerId(@Param("peerId") String peerId);

}
