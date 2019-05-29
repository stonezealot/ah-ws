package com.epb.ah.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epb.ah.entity.EcbestSkuView;

public interface EcbestSkuViewRepository
		extends JpaRepository<EcbestSkuView, BigDecimal> {

	List<EcbestSkuView> findByOrgId(final String orgId);
	
}
