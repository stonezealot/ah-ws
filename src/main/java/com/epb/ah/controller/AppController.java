package com.epb.ah.controller;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epb.ah.bean.AddToCartPayload;
import com.epb.ah.bean.BookmarkPayload;
import com.epb.ah.bean.CartlineEditCashcarryPayload;
import com.epb.ah.bean.CartlineEditInstallationPayload;
import com.epb.ah.bean.CartlineEditQtyPayload;
import com.epb.ah.bean.CartlineQtyPayload;
import com.epb.ah.bean.CheckoutBillingPayload;
import com.epb.ah.bean.CheckoutDeliveryPayload;
import com.epb.ah.bean.CommonPayload;
import com.epb.ah.bean.CustomerChangePasswordPayload;
import com.epb.ah.bean.CustomerUpdatePayload;
import com.epb.ah.bean.EcstkInfo;
import com.epb.ah.entity.Customer;
import com.epb.ah.entity.EcDeliveryTimeslot;
import com.epb.ah.entity.Ecbanner;
import com.epb.ah.entity.Ecbest;
import com.epb.ah.entity.EcbestSkuView;
import com.epb.ah.entity.EcbookmarkView;
import com.epb.ah.entity.Eccart;
import com.epb.ah.entity.EccartlineView;
import com.epb.ah.entity.Eccat;
import com.epb.ah.entity.Ecordermas;
import com.epb.ah.entity.EcskuOverviewPicture;
import com.epb.ah.entity.EcskuSpecPicture;
import com.epb.ah.entity.Ecstk;
import com.epb.ah.entity.Ewallet;
import com.epb.ah.entity.EwalletDtl;
import com.epb.ah.entity.Ppcard;
import com.epb.ah.entity.PpcardLog;
import com.epb.ah.repository.EcstkRepository;
import com.epb.ah.repository.EwalletDtlRepository;
import com.epb.ah.repository.EwalletRepository;
import com.epb.ah.repository.PpcardLogRepository;
import com.epb.ah.repository.PpcardRepository;
import com.epb.ah.repository.CustomerRepository;
import com.epb.ah.repository.EcDeliveryTimeslotRepository;
import com.epb.ah.repository.EcbannerRepository;
import com.epb.ah.repository.EcbestRepository;
import com.epb.ah.repository.EcbestSkuViewRepository;
import com.epb.ah.repository.EcbookmarkRepository;
import com.epb.ah.repository.EccartRepository;
import com.epb.ah.repository.EccartlineViewRepository;
import com.epb.ah.repository.EccatRepository;
import com.epb.ah.repository.EcordermasRepository;
import com.epb.ah.repository.EcskuOverviewPictureRepository;
import com.epb.ah.repository.EcskuSpecPictureRepository;
import com.epb.ah.service.ProcedureResponse;
import com.epb.ah.service.ProcedureService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AppController {

	//
	// request mappings
	//

	@GetMapping("/stocks")
	public ResponseEntity<List<Ecstk>> getEcstks(
			@RequestParam final String orgId) {

		final Ecstk probe = new Ecstk();
		probe.setOrgId(orgId);
		// on optional sectionId
		final List<Ecstk> ecstks = this.ecstkRepository
				.findAll(
						Example.of(probe),
						Sort.by("stkId"));
		return ResponseEntity.ok(ecstks);
	}

	@GetMapping("/stocks-net-price-asc")
	public ResponseEntity<List<Ecstk>> getEcstksNetPriceAsc(
			@RequestParam final String orgId) {

		final Ecstk probe = new Ecstk();
		probe.setOrgId(orgId);
		// on optional sectionId
		final List<Ecstk> ecstks = this.ecstkRepository
				.findAll(
						Example.of(probe),
						Sort.by("netPrice"));
		return ResponseEntity.ok(ecstks);
	}

	@GetMapping("/stocks-net-price-desc")
	public ResponseEntity<List<Ecstk>> getEcstksNetPriceDesc(
			@RequestParam final String orgId) {

		final Ecstk probe = new Ecstk();
		probe.setOrgId(orgId);

		// on optional sectionId
		final List<Ecstk> ecstks = this.ecstkRepository
				.findAll(
						Example.of(probe),
						Sort.by(Sort.Direction.DESC, "netPrice"));
		return ResponseEntity.ok(ecstks);
	}

	@GetMapping("/categorys")
	public ResponseEntity<List<Eccat>> getCategorys(
			@RequestParam final String orgId) {

		final List<Eccat> eccats = this.eccatRepository
				.findByOrgIdOrderBySortNum(orgId);

		return ResponseEntity.ok(eccats);
	}

	@GetMapping("/stocks/{recKey}")
	public ResponseEntity<EcstkInfo> getEcstkInfo(
			@PathVariable final BigDecimal recKey) {
		final Optional<Ecstk> ecstk = this.ecstkRepository
				.findById(recKey);
		if (!ecstk.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		final EcstkInfo ecstkInfo = new EcstkInfo(
				this.getEcstkFromJDBC(recKey));

		return ResponseEntity.ok(ecstkInfo);
	}

	@GetMapping("/customer")
	public ResponseEntity<List<Customer>> getCustomer(
			@RequestParam final String custId,
			@RequestParam final String orgId) {

		final List<Customer> customer = this.customerRepository
				.findByCustIdAndOrgId(custId, orgId);

		return ResponseEntity.ok(customer);
	}

	@GetMapping("/carts")
	public ResponseEntity<List<Eccart>> getEccarts(
			@RequestParam final String custId,
			@RequestParam final String ecshopId) {

		final List<Eccart> eccarts = this.eccartRepository
				.findByCustIdAndEcshopId(custId, ecshopId);

		return ResponseEntity.ok(eccarts);
	}

	@GetMapping("/cartlines")
	public ResponseEntity<List<EccartlineView>> getEccartlines(
			@RequestParam final String custId,
			@RequestParam final String ecshopId) {

		final List<EccartlineView> eccartlineViews = this.eccartlineViewRepository
				.findByCustIdAndEcshopId(custId, ecshopId);

		return ResponseEntity.ok(eccartlineViews);
	}

	@GetMapping("/bookmarks")
	public ResponseEntity<List<EcbookmarkView>> getBookmarks(
			@RequestParam final String custId,
			@RequestParam final String ecshopId) {

		final List<EcbookmarkView> ecbookmarkViews = this.ecbookmarkRepository
				.findByCustIdAndEcshopId(custId, ecshopId);

		return ResponseEntity.ok(ecbookmarkViews);
	}

	@GetMapping("/overviews")
	public ResponseEntity<List<EcskuOverviewPicture>> getOverviewPictures(
			@RequestParam final String stkId,
			@RequestParam(required = false) final String orgId) {
		final EcskuOverviewPicture probe = new EcskuOverviewPicture();
		probe.setStkId(stkId);

		if (orgId != null && !orgId.isEmpty()) {
			probe.setOrgId(orgId);
		}

		final List<EcskuOverviewPicture> ecskuOverviewPictures = this.ecskuOverviewPictureRepository
				.findAll(
						Example.of(probe),
						Sort.by("sortNum"));

		return ResponseEntity.ok(ecskuOverviewPictures);
	}

	@GetMapping("/specs")
	public ResponseEntity<List<EcskuSpecPicture>> getSpecPictures(
			@RequestParam final String stkId,
			@RequestParam(required = false) final String orgId) {
		final EcskuSpecPicture probe = new EcskuSpecPicture();
		probe.setStkId(stkId);

		if (orgId != null && !orgId.isEmpty()) {
			probe.setOrgId(orgId);
		}

		final List<EcskuSpecPicture> ecskuSpecPictures = this.ecskuSpecPictureRepository
				.findAll(
						Example.of(probe),
						Sort.by("sortNum"));

		return ResponseEntity.ok(ecskuSpecPictures);
	}

	@GetMapping("/orders")
	public ResponseEntity<List<Ecordermas>> getOrders(
			@RequestParam final String custId,
			@RequestParam final String ecshopId) {

		final List<Ecordermas> ecordermases = this.ecordermasRepository
				.findByVipIdAndEcshopIdOrderByRecKeyDesc(custId, ecshopId);

		return ResponseEntity.ok(ecordermases);
	}

	@GetMapping("/ewallets")
	public ResponseEntity<List<Ewallet>> getEwallets(
			@RequestParam final String orgId,
			@RequestParam final String custId) {

		final List<Ewallet> ewallets = this.ewalletRepository
				.findByOrgIdAndCustId(orgId, custId);

		return ResponseEntity.ok(ewallets);
	}

	@GetMapping("/ewallet-details")
	public ResponseEntity<List<EwalletDtl>> getEwalletDtls(
			@RequestParam final String custId,
			@RequestParam final String orgId) {

		final List<EwalletDtl> ewalletDtls = this.ewalletDtlRepository
				.findByCustIdAndOrgIdOrderBySrcDocDateDesc(custId, orgId);

		return ResponseEntity.ok(ewalletDtls);
	}

	@GetMapping("/credits")
	public ResponseEntity<List<Ppcard>> getCredits(
			@RequestParam final String custId,
			@RequestParam final String orgId) {

		final List<Ppcard> ppcards = this.ppcardRepository
				.findByPpIdAndOrgId(custId, orgId);

		return ResponseEntity.ok(ppcards);
	}

	@GetMapping("/credit-details")
	public ResponseEntity<List<PpcardLog>> getCreditDtls(
			@RequestParam final String custId,
			@RequestParam final String orgId) {

		final List<PpcardLog> ppcardLogs = this.ppcardLogRepository
				.findByPpIdAndOrgIdOrderBySrcDocDateDesc(custId, orgId);

		return ResponseEntity.ok(ppcardLogs);
	}

	@GetMapping("/timeslots")
	public ResponseEntity<List<EcDeliveryTimeslot>> getDeliveryTimeslots(
			@RequestParam final String orgId,
			@RequestParam final String dlyZoneId) {

		final EcDeliveryTimeslot probe = new EcDeliveryTimeslot();
		probe.setOrgId(orgId);
		probe.setDlyZoneId(dlyZoneId);

		final List<EcDeliveryTimeslot> ecDeliveryTimeslots = this.ecDeliveryTimeslotRepository
				.findAll(
						Example.of(probe),
						Sort.by("dlyDate", "timeslotId"));

		return ResponseEntity.ok(ecDeliveryTimeslots);
	}

	@GetMapping("/banners")
	public ResponseEntity<List<Ecbanner>> getEcbanners(
			@RequestParam final String orgId) {

		final List<Ecbanner> ecbanners = this.ecbannerRepository
				.findByOrgIdOrderByBannerId(orgId);

		return ResponseEntity.ok(ecbanners);
	}

	@GetMapping("/bests")
	public ResponseEntity<List<Ecbest>> getEcbests(
			@RequestParam final Character statusFlg,
			@RequestParam final String orgId) {

		final List<Ecbest> Ecbests = this.ecbestRepository
				.findByStatusFlgAndOrgId(statusFlg, orgId);

		return ResponseEntity.ok(Ecbests);
	}

	@GetMapping("/best-sku-views")
	public ResponseEntity<List<EcbestSkuView>> getEcbestSkuViews(
			@RequestParam final String orgId) {

		final List<EcbestSkuView> ecbestSkuViews = this.ecbestSkuViewRepository
				.findByOrgId(orgId);

		return ResponseEntity.ok(ecbestSkuViews);
	}

	@PostMapping("/customer/{recKey}/update")
	public ResponseEntity<List<Customer>> customerUpdate(
			@PathVariable final String recKey,
			@RequestBody final CustomerUpdatePayload payload) {

		final ProcedureResponse response = this.procedureService
				.eccustUpdate(
						"",
						recKey,
						payload.getCustName(),
						payload.getEmail(),
						payload.getPhone(),
						payload.getAddr1(),
						payload.getAddr2(),
						payload.getPostalcode());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getCustomer(payload.getCustId(), payload.getOrgId());

	}

	@PostMapping("/customer/{recKey}/change-password")
	public ResponseEntity<List<Customer>> customerChangePassword(
			@PathVariable final String recKey,
			@RequestBody final CustomerChangePasswordPayload payload) {

		final String oldPassword = this.toUserPwd(payload.getOldPassword());
		final String newPassword = this.toUserPwd(payload.getNewPassword());

		final ProcedureResponse response = this.procedureService
				.ecChangePassword(
						"",
						recKey,
						oldPassword,
						newPassword);

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getCustomer(payload.getCustId(), payload.getOrgId());

	}

	@PostMapping("/cartlines/{recKey}/qty-plus")
	public ResponseEntity<List<EccartlineView>> cartlineQtyPlus(
			@PathVariable final String recKey,
			@RequestBody final CartlineQtyPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecEditCartInc(
						"",
						recKey,
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId());
		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccartlines(payload.getCustId(), payload.getEcshopId());

	}

	@PostMapping("/cartlines/{recKey}/qty-minus")
	public ResponseEntity<List<EccartlineView>> cartlineQtyMinus(
			@PathVariable final String recKey,
			@RequestBody final CartlineQtyPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecEditCartDec(
						"",
						recKey,
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId());
		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccartlines(payload.getCustId(), payload.getEcshopId());

	}

	@PostMapping("/cartlines/{recKey}/edit-qty")
	public ResponseEntity<List<EccartlineView>> cartlineEditQty(
			@PathVariable final String recKey,
			@RequestBody final CartlineEditQtyPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecEditCartQty(
						"",
						recKey,
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId(),
						payload.getStkId(),
						payload.getQty());
		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccartlines(payload.getCustId(), payload.getEcshopId());

	}

	@PostMapping("/cartlines/{recKey}/edit-installation")
	public ResponseEntity<List<EccartlineView>> cartlineEditInstallation(
			@PathVariable final String recKey,
			@RequestBody final CartlineEditInstallationPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecEditCartInstallation(
						"",
						recKey,
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId(),
						payload.getStkId(),
						payload.getInstallation());
		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccartlines(payload.getCustId(), payload.getEcshopId());

	}

	@PostMapping("/cartlines/{recKey}/edit-cashcarry")
	public ResponseEntity<List<EccartlineView>> cartlineEditCashcarry(
			@PathVariable final String recKey,
			@RequestBody final CartlineEditCashcarryPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecEditCartCashcarry(
						"",
						recKey,
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId(),
						payload.getStkId(),
						payload.getCashcarry());
		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccartlines(payload.getCustId(), payload.getEcshopId());

	}

	@PostMapping("/cartlines/{recKey}/delete")
	public ResponseEntity<List<EccartlineView>> cartlineDelete(
			@PathVariable final String recKey,
			@RequestBody final CartlineQtyPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecDeleteCart(
						"",
						recKey,
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccartlines(payload.getCustId(), payload.getEcshopId());

	}

	@PostMapping("/add-to-cart")
	public ResponseEntity<List<EccartlineView>> addToCart(
			@RequestBody final AddToCartPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecAddCart(
						"",
						payload.getOrgId(),
						payload.getCustId(),
						payload.getGuestFlg(),
						payload.getEcshopId(),
						payload.getStkId(),
						payload.getQty(),
						payload.getCashcarry(),
						payload.getInstallationFlg());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccartlines(payload.getCustId(), payload.getEcshopId());
	}

	@PostMapping("/add-bookmark")
	public ResponseEntity<List<EcbookmarkView>> addBookmark(
			@RequestBody final BookmarkPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecAddBookmark(
						"",
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId(),
						payload.getStkId());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getBookmarks(payload.getCustId(), payload.getEcshopId());
	}

	@PostMapping("/delete-bookmark")
	public ResponseEntity<List<EcbookmarkView>> deleteBookmark(
			@RequestBody final BookmarkPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecDeleteBookmark(
						"",
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId(),
						payload.getStkId());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getBookmarks(payload.getCustId(), payload.getEcshopId());
	}

	@PostMapping("/checkout")
	public ResponseEntity<List<Eccart>> checkout(
			@RequestBody final CommonPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecCartCheckout(
						"",
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccarts(payload.getCustId(), payload.getEcshopId());
	}

	@PostMapping("/recalculate")
	public ResponseEntity<List<Eccart>> recalculate(
			@RequestBody final CommonPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecCartRecalculate(
						"",
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccarts(payload.getCustId(), payload.getEcshopId());
	}

	@PostMapping("/checkout-delivery")
	public ResponseEntity<List<Eccart>> checkoutDelivery(
			@RequestBody final CheckoutDeliveryPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecCheckoutDeliveryAction(
						"",
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId(),
						payload.getDlyZoneId(),
						payload.getDlyDate(),
						payload.getTimeslotId(),
						payload.getAddr1(),
						payload.getAddr2(),
						payload.getPostalcode(),
						payload.getRemark());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccarts(payload.getCustId(), payload.getEcshopId());
	}

	@PostMapping("/checkout-billing")
	public ResponseEntity<List<Eccart>> checkoutBilling(
			@RequestBody final CheckoutBillingPayload payload) {

		final ProcedureResponse response = this.procedureService
				.ecCheckoutBillingAction(
						"",
						payload.getOrgId(),
						payload.getCustId(),
						payload.getEcshopId(),
						payload.getAddressFlg(),
						payload.getAddr1(),
						payload.getAddr2(),
						payload.getPostalcode(),
						payload.getEwalletRedeem());

		if (!ProcedureService.ERR_CODE_OK.equals(response.getErrCode())) {
			throw new RuntimeException(response.getErrMsg());
		}

		return this.getEccarts(payload.getCustId(), payload.getEcshopId());
	}
	//
	// private methods
	//

	private Ecstk getEcstkFromJDBC(final BigDecimal recKey) {
		// for situations where procedure (JDBC) has just updated the table
		return this.jdbcTemplate.queryForObject(
				"select * from ECSTK where rec_key = ?",
				new Object[] { recKey },
				BeanPropertyRowMapper.newInstance(Ecstk.class));
	}

	private String toUserPwd(final String password) {
		try {
			if (password == null) {
				return null;
			}

			final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
			final byte[] digests = messageDigest.digest(password.getBytes());

			final StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < digests.length; i++) {
				int halfbyte = (digests[i] >>> 4) & 0x0F;
				for (int j = 0; j <= 1; j++) {
					stringBuilder.append(
							((0 <= halfbyte) && (halfbyte <= 9))
									? (char) ('0' + halfbyte)
									: (char) ('a' + (halfbyte - 10)));
					halfbyte = digests[i] & 0x0F;
				}
			}

			return stringBuilder.toString();
		} catch (final Throwable throwable) {
			this.log.error("error converting password", throwable);
			return null;
		}
	}

	//
	// fields
	//

	private final Log log = LogFactory.getLog(AppController.class);

	private final JdbcTemplate jdbcTemplate;

	private final CustomerRepository customerRepository;
	private final EcstkRepository ecstkRepository;
	private final EccatRepository eccatRepository;
	private final EccartRepository eccartRepository;
	private final EccartlineViewRepository eccartlineViewRepository;
	private final EcskuOverviewPictureRepository ecskuOverviewPictureRepository;
	private final EcskuSpecPictureRepository ecskuSpecPictureRepository;
	private final EcbookmarkRepository ecbookmarkRepository;
	private final EcordermasRepository ecordermasRepository;
	private final EwalletRepository ewalletRepository;
	private final EwalletDtlRepository ewalletDtlRepository;
	private final PpcardRepository ppcardRepository;
	private final PpcardLogRepository ppcardLogRepository;
	private final EcDeliveryTimeslotRepository ecDeliveryTimeslotRepository;
	private final EcbannerRepository ecbannerRepository;
	private final EcbestRepository ecbestRepository;
	private final EcbestSkuViewRepository ecbestSkuViewRepository;

	private final ProcedureService procedureService;

	private final String dateFormatPattern = "yyyy-MM-dd";
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
			this.dateFormatPattern);

	//
	// constructor
	//

	@Autowired
	public AppController(
			final JdbcTemplate jdbcTemplate,
			final ProcedureService procedureService,
			final CustomerRepository customerRepository,
			final EcstkRepository ecstkRepository,
			final EccatRepository eccatRepository,
			final EccartRepository eccartRepository,
			final EccartlineViewRepository eccartlineViewRepository,
			final EcskuOverviewPictureRepository ecskuOverviewPictureRepository,
			final EcskuSpecPictureRepository ecskuSpecPictureRepository,
			final EcbookmarkRepository ecbookmarkRepository,
			final EcordermasRepository ecordermasRepository,
			final EwalletRepository ewalletRepository,
			final EwalletDtlRepository ewalletDtlRepository,
			final PpcardRepository ppcardRepository,
			final PpcardLogRepository ppcardLogRepository,
			final EcDeliveryTimeslotRepository ecDeliveryTimeslotRepository,
			final EcbannerRepository ecbannerRepository,
			final EcbestRepository ecbestRepository,
			final EcbestSkuViewRepository ecbestSkuViewRepository) {

		super();

		this.jdbcTemplate = jdbcTemplate;
		this.jdbcTemplate.setResultsMapCaseInsensitive(true);

		this.procedureService = procedureService;

		this.customerRepository = customerRepository;
		this.ecstkRepository = ecstkRepository;
		this.eccatRepository = eccatRepository;
		this.eccartRepository = eccartRepository;
		this.eccartlineViewRepository = eccartlineViewRepository;
		this.ecskuOverviewPictureRepository = ecskuOverviewPictureRepository;
		this.ecskuSpecPictureRepository = ecskuSpecPictureRepository;
		this.ecbookmarkRepository = ecbookmarkRepository;
		this.ecordermasRepository = ecordermasRepository;
		this.ewalletRepository = ewalletRepository;
		this.ewalletDtlRepository = ewalletDtlRepository;
		this.ppcardRepository = ppcardRepository;
		this.ppcardLogRepository = ppcardLogRepository;
		this.ecDeliveryTimeslotRepository = ecDeliveryTimeslotRepository;
		this.ecbannerRepository = ecbannerRepository;
		this.ecbestRepository = ecbestRepository;
		this.ecbestSkuViewRepository = ecbestSkuViewRepository;

	}

}
