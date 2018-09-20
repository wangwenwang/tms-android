package com.kaidongyuan.app.kdydriver.bean.order;

import java.util.Date;

public class Order_bakup implements java.io.Serializable {
	private Date orderTimeStart;
	private Date orderTimeEnd;

	public Date getOrderTimeStart() {
		return orderTimeStart;
	}

	public void setOrderTimeStart(Date orderTimeStart) {
		this.orderTimeStart = orderTimeStart;
	}

	public Date getOrderTimeEnd() {
		return orderTimeEnd;
	}

	public void setOrderTimeEnd(Date orderTimeEnd) {
		this.orderTimeEnd = orderTimeEnd;
	}

	private int page;
	private int rows;
	private String sort;
	private String order;
	private String ids;
	private String q;

	private Long idx;
	private Long ordEntIdx;
	private Long ordOrgIdx;
	private Long ordEdiIdx;
	private Long ordBusinessIdx;
	private String ordGroupNo;
	private String ordNo;
	private String ordNoClient;
	private String ordNoConsignee;
	private String ordState;
	private String ordWorkflow;
	private String ordType;
	private String ordTypeClient;
	private String ordTypeHandling;
	private String ordTypeTransport;
	private String ordTypeConsign;
	private String ordClass;
	private String ordMarks;
	private String ordWavegroup;
	private String ordFlagTransport;
	private String ordFlagStorage;
	private String ordFlagUrgent;
	private String ordFlagReturn;
	private String ordRemarkClient;
	private String ordRemarkConsignee;
	private String ordOperationRecord;
	private String versionNumber;
	private Long ordOperatorIdx;
	private Date ordDateNotice;
	private Date ordDateAdd;
	private Date ordDateEdit;
	private String ordShipmentNo;
	private String ordVehicleType;
	private String ordVehicleSize;
	private Date ordRequestIssue;
	private Date ordRequestDelivery;
	private String ordBillType;
	private String ordPayerType;
	private Date ordProjectedDelivery;
	private String ordFromCode;
	private String ordFromName;
	private String ordFromAddress;
	private String ordFromProperty;
	private String ordFromCname;
	private String ordFromCtel;
	private String ordFromCsms;
	private String ordFromCountry;
	private String ordFromProvince;
	private String ordFromCity;
	private String ordFromRegion;
	private String ordFromZip;
	private Long ordFromSite;
	private String ordToCode;
	private String ordToName;
	private String ordToAddress;
	private String ordToProperty;
	private String ordToCname;
	private String ordToCtel;
	private String ordToCsms;
	private String ordToCountry;
	private String ordToProvince;
	private String ordToCity;
	private String ordToRegion;
	private String ordToZip;
	private Long ordToSite;
	private Double ordQty;
	private Double ordWeight;
	private Double ordVolume;
	private Double ordIssueQty;
	private Double ordIssueWeight;
	private Double ordIssueVolume;
	private Byte ordOrderCount;
	private String ordCopyFlag;
	private Double ordContractPrice;
	private Double ordNegotiatedPrice;
	private String ordDifferenceReason;
	private Double ordManualWeight;
	private Double ordManualVolume;
	private Double ordQtyDelivery;
	private Double ordQtyReject;
	private Double ordQtyAbeyance;
	private Double ordQtyMissing;
	private Double ordQtyDamage;
	private Long sfNo;
	private String omsParentNo;
	private String omsSplitType;
	private String omsSplitReason;
	private String omsRemarkAudit;
	private Date omsReservationStart;
	private Date omsReservationEnd;
	private String omsReservationPerson;
	private Date omsReservationDate;
	private Date tmsArriveExpected;
	private Date tmsArriveActual;
	private Date tmsLoadStart;
	private Date tmsLoadEnd;
	private Date tmsDepartExpected;
	private String tmsRemarkDispatch;
	private Date tmsDateLoad;
	private Date tmsDateConfirmed;
	private Date tmsDateIssue;
	private String tmsShipmentNo;
	private Short tmsDropPoint;
	private String tmsWearhouseNumber;
	private String tmsTypeTransport;
	private String tmsTypeConsign;
	private Long tmsFleetIdx;
	private String tmsFleetName;
	private Long tmsVehicleIdx;
	private String tmsPlateNumber;
	private String tmsVehicleType;
	private String tmsVehicleSize;
	private Long tmsDriverIdx;
	private String tmsDriverName;
	private String tmsDriverTel;
	private String otsRemarkDelivery;
	private String otsRemarkReturn;
	private Date otsDeliveryNotice;
	private Date otsDeliveryActual;
	private Date otsReturnDate;
	private Long otsDaysKpi;
	private Long otsDaysReturn;
	private String otsReasonDelivery;
	private String otsReasonReturn;
	private String otsReasonPart;
	private String otsDeliveryQualified;
	private String otsDeliveryComplete;
	private String otsReturnQualified;
	private String bmsFlag;
	private String reference01;
	private String reference02;
	private Date reference03;
	private String reference04;
	private String reference05;
	private Date reference06;
	private String reference07;
	private String reference08;
	private String reference09;
	private String reference10;
	private String update01;
	private String update02;
	private String update03;
	private String update04;
	private String update05;
	private String update06;
	private Double ordPalletQty;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getOrdEntIdx() {
		return ordEntIdx;
	}

	public void setOrdEntIdx(Long ordEntIdx) {
		this.ordEntIdx = ordEntIdx;
	}

	public Long getOrdOrgIdx() {
		return ordOrgIdx;
	}

	public void setOrdOrgIdx(Long ordOrgIdx) {
		this.ordOrgIdx = ordOrgIdx;
	}

	public Long getOrdEdiIdx() {
		return ordEdiIdx;
	}

	public void setOrdEdiIdx(Long ordEdiIdx) {
		this.ordEdiIdx = ordEdiIdx;
	}

	public Long getOrdBusinessIdx() {
		return ordBusinessIdx;
	}

	public void setOrdBusinessIdx(Long ordBusinessIdx) {
		this.ordBusinessIdx = ordBusinessIdx;
	}

	public String getOrdGroupNo() {
		return ordGroupNo;
	}

	public void setOrdGroupNo(String ordGroupNo) {
		this.ordGroupNo = ordGroupNo;
	}

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}

	public String getOrdNoClient() {
		return ordNoClient;
	}

	public void setOrdNoClient(String ordNoClient) {
		this.ordNoClient = ordNoClient;
	}

	public String getOrdNoConsignee() {
		return ordNoConsignee;
	}

	public void setOrdNoConsignee(String ordNoConsignee) {
		this.ordNoConsignee = ordNoConsignee;
	}

	public String getOrdState() {
		return ordState;
	}

	public void setOrdState(String ordState) {
		this.ordState = ordState;
	}

	public String getOrdWorkflow() {
		return ordWorkflow;
	}

	public void setOrdWorkflow(String ordWorkflow) {
		this.ordWorkflow = ordWorkflow;
	}

	public String getOrdType() {
		return ordType;
	}

	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}

	public String getOrdTypeClient() {
		return ordTypeClient;
	}

	public void setOrdTypeClient(String ordTypeClient) {
		this.ordTypeClient = ordTypeClient;
	}

	public String getOrdTypeHandling() {
		return ordTypeHandling;
	}

	public void setOrdTypeHandling(String ordTypeHandling) {
		this.ordTypeHandling = ordTypeHandling;
	}

	public String getOrdTypeTransport() {
		return ordTypeTransport;
	}

	public void setOrdTypeTransport(String ordTypeTransport) {
		this.ordTypeTransport = ordTypeTransport;
	}

	public String getOrdTypeConsign() {
		return ordTypeConsign;
	}

	public void setOrdTypeConsign(String ordTypeConsign) {
		this.ordTypeConsign = ordTypeConsign;
	}

	public String getOrdClass() {
		return ordClass;
	}

	public void setOrdClass(String ordClass) {
		this.ordClass = ordClass;
	}

	public String getOrdMarks() {
		return ordMarks;
	}

	public void setOrdMarks(String ordMarks) {
		this.ordMarks = ordMarks;
	}

	public String getOrdWavegroup() {
		return ordWavegroup;
	}

	public void setOrdWavegroup(String ordWavegroup) {
		this.ordWavegroup = ordWavegroup;
	}

	public String getOrdFlagTransport() {
		return ordFlagTransport;
	}

	public void setOrdFlagTransport(String ordFlagTransport) {
		this.ordFlagTransport = ordFlagTransport;
	}

	public String getOrdFlagStorage() {
		return ordFlagStorage;
	}

	public void setOrdFlagStorage(String ordFlagStorage) {
		this.ordFlagStorage = ordFlagStorage;
	}

	public String getOrdFlagUrgent() {
		return ordFlagUrgent;
	}

	public void setOrdFlagUrgent(String ordFlagUrgent) {
		this.ordFlagUrgent = ordFlagUrgent;
	}

	public String getOrdFlagReturn() {
		return ordFlagReturn;
	}

	public void setOrdFlagReturn(String ordFlagReturn) {
		this.ordFlagReturn = ordFlagReturn;
	}

	public String getOrdRemarkClient() {
		return ordRemarkClient;
	}

	public void setOrdRemarkClient(String ordRemarkClient) {
		this.ordRemarkClient = ordRemarkClient;
	}

	public String getOrdRemarkConsignee() {
		return ordRemarkConsignee;
	}

	public void setOrdRemarkConsignee(String ordRemarkConsignee) {
		this.ordRemarkConsignee = ordRemarkConsignee;
	}

	public String getOrdOperationRecord() {
		return ordOperationRecord;
	}

	public void setOrdOperationRecord(String ordOperationRecord) {
		this.ordOperationRecord = ordOperationRecord;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Long getOrdOperatorIdx() {
		return ordOperatorIdx;
	}

	public void setOrdOperatorIdx(Long ordOperatorIdx) {
		this.ordOperatorIdx = ordOperatorIdx;
	}

	public Date getOrdDateNotice() {
		return ordDateNotice;
	}

	public void setOrdDateNotice(Date ordDateNotice) {
		this.ordDateNotice = ordDateNotice;
	}

	public Date getOrdDateAdd() {
		return ordDateAdd;
	}

	public void setOrdDateAdd(Date ordDateAdd) {
		this.ordDateAdd = ordDateAdd;
	}

	public Date getOrdDateEdit() {
		return ordDateEdit;
	}

	public void setOrdDateEdit(Date ordDateEdit) {
		this.ordDateEdit = ordDateEdit;
	}

	public String getOrdShipmentNo() {
		return ordShipmentNo;
	}

	public void setOrdShipmentNo(String ordShipmentNo) {
		this.ordShipmentNo = ordShipmentNo;
	}

	public String getOrdVehicleType() {
		return ordVehicleType;
	}

	public void setOrdVehicleType(String ordVehicleType) {
		this.ordVehicleType = ordVehicleType;
	}

	public String getOrdVehicleSize() {
		return ordVehicleSize;
	}

	public void setOrdVehicleSize(String ordVehicleSize) {
		this.ordVehicleSize = ordVehicleSize;
	}

	public Date getOrdRequestIssue() {
		return ordRequestIssue;
	}

	public void setOrdRequestIssue(Date ordRequestIssue) {
		this.ordRequestIssue = ordRequestIssue;
	}

	public Date getOrdRequestDelivery() {
		return ordRequestDelivery;
	}

	public void setOrdRequestDelivery(Date ordRequestDelivery) {
		this.ordRequestDelivery = ordRequestDelivery;
	}

	public String getOrdBillType() {
		return ordBillType;
	}

	public void setOrdBillType(String ordBillType) {
		this.ordBillType = ordBillType;
	}

	public String getOrdPayerType() {
		return ordPayerType;
	}

	public void setOrdPayerType(String ordPayerType) {
		this.ordPayerType = ordPayerType;
	}

	public Date getOrdProjectedDelivery() {
		return ordProjectedDelivery;
	}

	public void setOrdProjectedDelivery(Date ordProjectedDelivery) {
		this.ordProjectedDelivery = ordProjectedDelivery;
	}

	public String getOrdFromCode() {
		return ordFromCode;
	}

	public void setOrdFromCode(String ordFromCode) {
		this.ordFromCode = ordFromCode;
	}

	public String getOrdFromName() {
		return ordFromName;
	}

	public void setOrdFromName(String ordFromName) {
		this.ordFromName = ordFromName;
	}

	public String getOrdFromAddress() {
		return ordFromAddress;
	}

	public void setOrdFromAddress(String ordFromAddress) {
		this.ordFromAddress = ordFromAddress;
	}

	public String getOrdFromProperty() {
		return ordFromProperty;
	}

	public void setOrdFromProperty(String ordFromProperty) {
		this.ordFromProperty = ordFromProperty;
	}

	public String getOrdFromCname() {
		return ordFromCname;
	}

	public void setOrdFromCname(String ordFromCname) {
		this.ordFromCname = ordFromCname;
	}

	public String getOrdFromCtel() {
		return ordFromCtel;
	}

	public void setOrdFromCtel(String ordFromCtel) {
		this.ordFromCtel = ordFromCtel;
	}

	public String getOrdFromCsms() {
		return ordFromCsms;
	}

	public void setOrdFromCsms(String ordFromCsms) {
		this.ordFromCsms = ordFromCsms;
	}

	public String getOrdFromCountry() {
		return ordFromCountry;
	}

	public void setOrdFromCountry(String ordFromCountry) {
		this.ordFromCountry = ordFromCountry;
	}

	public String getOrdFromProvince() {
		return ordFromProvince;
	}

	public void setOrdFromProvince(String ordFromProvince) {
		this.ordFromProvince = ordFromProvince;
	}

	public String getOrdFromCity() {
		return ordFromCity;
	}

	public void setOrdFromCity(String ordFromCity) {
		this.ordFromCity = ordFromCity;
	}

	public String getOrdFromRegion() {
		return ordFromRegion;
	}

	public void setOrdFromRegion(String ordFromRegion) {
		this.ordFromRegion = ordFromRegion;
	}

	public String getOrdFromZip() {
		return ordFromZip;
	}

	public void setOrdFromZip(String ordFromZip) {
		this.ordFromZip = ordFromZip;
	}

	public Long getOrdFromSite() {
		return ordFromSite;
	}

	public void setOrdFromSite(Long ordFromSite) {
		this.ordFromSite = ordFromSite;
	}

	public String getOrdToCode() {
		return ordToCode;
	}

	public void setOrdToCode(String ordToCode) {
		this.ordToCode = ordToCode;
	}

	public String getOrdToName() {
		return ordToName;
	}

	public void setOrdToName(String ordToName) {
		this.ordToName = ordToName;
	}

	public String getOrdToAddress() {
		return ordToAddress;
	}

	public void setOrdToAddress(String ordToAddress) {
		this.ordToAddress = ordToAddress;
	}

	public String getOrdToProperty() {
		return ordToProperty;
	}

	public void setOrdToProperty(String ordToProperty) {
		this.ordToProperty = ordToProperty;
	}

	public String getOrdToCname() {
		return ordToCname;
	}

	public void setOrdToCname(String ordToCname) {
		this.ordToCname = ordToCname;
	}

	public String getOrdToCtel() {
		return ordToCtel;
	}

	public void setOrdToCtel(String ordToCtel) {
		this.ordToCtel = ordToCtel;
	}

	public String getOrdToCsms() {
		return ordToCsms;
	}

	public void setOrdToCsms(String ordToCsms) {
		this.ordToCsms = ordToCsms;
	}

	public String getOrdToCountry() {
		return ordToCountry;
	}

	public void setOrdToCountry(String ordToCountry) {
		this.ordToCountry = ordToCountry;
	}

	public String getOrdToProvince() {
		return ordToProvince;
	}

	public void setOrdToProvince(String ordToProvince) {
		this.ordToProvince = ordToProvince;
	}

	public String getOrdToCity() {
		return ordToCity;
	}

	public void setOrdToCity(String ordToCity) {
		this.ordToCity = ordToCity;
	}

	public String getOrdToRegion() {
		return ordToRegion;
	}

	public void setOrdToRegion(String ordToRegion) {
		this.ordToRegion = ordToRegion;
	}

	public String getOrdToZip() {
		return ordToZip;
	}

	public void setOrdToZip(String ordToZip) {
		this.ordToZip = ordToZip;
	}

	public Long getOrdToSite() {
		return ordToSite;
	}

	public void setOrdToSite(Long ordToSite) {
		this.ordToSite = ordToSite;
	}

	public Double getOrdQty() {
		return ordQty;
	}

	public void setOrdQty(Double ordQty) {
		this.ordQty = ordQty;
	}

	public Double getOrdWeight() {
		return ordWeight;
	}

	public void setOrdWeight(Double ordWeight) {
		this.ordWeight = ordWeight;
	}

	public Double getOrdVolume() {
		return ordVolume;
	}

	public void setOrdVolume(Double ordVolume) {
		this.ordVolume = ordVolume;
	}

	public Double getOrdIssueQty() {
		return ordIssueQty;
	}

	public void setOrdIssueQty(Double ordIssueQty) {
		this.ordIssueQty = ordIssueQty;
	}

	public Double getOrdIssueWeight() {
		return ordIssueWeight;
	}

	public void setOrdIssueWeight(Double ordIssueWeight) {
		this.ordIssueWeight = ordIssueWeight;
	}

	public Double getOrdIssueVolume() {
		return ordIssueVolume;
	}

	public void setOrdIssueVolume(Double ordIssueVolume) {
		this.ordIssueVolume = ordIssueVolume;
	}

	public Byte getOrdOrderCount() {
		return ordOrderCount;
	}

	public void setOrdOrderCount(Byte ordOrderCount) {
		this.ordOrderCount = ordOrderCount;
	}

	public String getOrdCopyFlag() {
		return ordCopyFlag;
	}

	public void setOrdCopyFlag(String ordCopyFlag) {
		this.ordCopyFlag = ordCopyFlag;
	}

	public Double getOrdContractPrice() {
		return ordContractPrice;
	}

	public void setOrdContractPrice(Double ordContractPrice) {
		this.ordContractPrice = ordContractPrice;
	}

	public Double getOrdNegotiatedPrice() {
		return ordNegotiatedPrice;
	}

	public void setOrdNegotiatedPrice(Double ordNegotiatedPrice) {
		this.ordNegotiatedPrice = ordNegotiatedPrice;
	}

	public String getOrdDifferenceReason() {
		return ordDifferenceReason;
	}

	public void setOrdDifferenceReason(String ordDifferenceReason) {
		this.ordDifferenceReason = ordDifferenceReason;
	}

	public Double getOrdManualWeight() {
		return ordManualWeight;
	}

	public void setOrdManualWeight(Double ordManualWeight) {
		this.ordManualWeight = ordManualWeight;
	}

	public Double getOrdManualVolume() {
		return ordManualVolume;
	}

	public void setOrdManualVolume(Double ordManualVolume) {
		this.ordManualVolume = ordManualVolume;
	}

	public Double getOrdQtyDelivery() {
		return ordQtyDelivery;
	}

	public void setOrdQtyDelivery(Double ordQtyDelivery) {
		this.ordQtyDelivery = ordQtyDelivery;
	}

	public Double getOrdQtyReject() {
		return ordQtyReject;
	}

	public void setOrdQtyReject(Double ordQtyReject) {
		this.ordQtyReject = ordQtyReject;
	}

	public Double getOrdQtyAbeyance() {
		return ordQtyAbeyance;
	}

	public void setOrdQtyAbeyance(Double ordQtyAbeyance) {
		this.ordQtyAbeyance = ordQtyAbeyance;
	}

	public Double getOrdQtyMissing() {
		return ordQtyMissing;
	}

	public void setOrdQtyMissing(Double ordQtyMissing) {
		this.ordQtyMissing = ordQtyMissing;
	}

	public Double getOrdQtyDamage() {
		return ordQtyDamage;
	}

	public void setOrdQtyDamage(Double ordQtyDamage) {
		this.ordQtyDamage = ordQtyDamage;
	}

	public Long getSfNo() {
		return sfNo;
	}

	public void setSfNo(Long sfNo) {
		this.sfNo = sfNo;
	}

	public String getOmsParentNo() {
		return omsParentNo;
	}

	public void setOmsParentNo(String omsParentNo) {
		this.omsParentNo = omsParentNo;
	}

	public String getOmsSplitType() {
		return omsSplitType;
	}

	public void setOmsSplitType(String omsSplitType) {
		this.omsSplitType = omsSplitType;
	}

	public String getOmsSplitReason() {
		return omsSplitReason;
	}

	public void setOmsSplitReason(String omsSplitReason) {
		this.omsSplitReason = omsSplitReason;
	}

	public String getOmsRemarkAudit() {
		return omsRemarkAudit;
	}

	public void setOmsRemarkAudit(String omsRemarkAudit) {
		this.omsRemarkAudit = omsRemarkAudit;
	}

	public Date getOmsReservationStart() {
		return omsReservationStart;
	}

	public void setOmsReservationStart(Date omsReservationStart) {
		this.omsReservationStart = omsReservationStart;
	}

	public Date getOmsReservationEnd() {
		return omsReservationEnd;
	}

	public void setOmsReservationEnd(Date omsReservationEnd) {
		this.omsReservationEnd = omsReservationEnd;
	}

	public String getOmsReservationPerson() {
		return omsReservationPerson;
	}

	public void setOmsReservationPerson(String omsReservationPerson) {
		this.omsReservationPerson = omsReservationPerson;
	}

	public Date getOmsReservationDate() {
		return omsReservationDate;
	}

	public void setOmsReservationDate(Date omsReservationDate) {
		this.omsReservationDate = omsReservationDate;
	}

	public Date getTmsArriveExpected() {
		return tmsArriveExpected;
	}

	public void setTmsArriveExpected(Date tmsArriveExpected) {
		this.tmsArriveExpected = tmsArriveExpected;
	}

	public Date getTmsArriveActual() {
		return tmsArriveActual;
	}

	public void setTmsArriveActual(Date tmsArriveActual) {
		this.tmsArriveActual = tmsArriveActual;
	}

	public Date getTmsLoadStart() {
		return tmsLoadStart;
	}

	public void setTmsLoadStart(Date tmsLoadStart) {
		this.tmsLoadStart = tmsLoadStart;
	}

	public Date getTmsLoadEnd() {
		return tmsLoadEnd;
	}

	public void setTmsLoadEnd(Date tmsLoadEnd) {
		this.tmsLoadEnd = tmsLoadEnd;
	}

	public Date getTmsDepartExpected() {
		return tmsDepartExpected;
	}

	public void setTmsDepartExpected(Date tmsDepartExpected) {
		this.tmsDepartExpected = tmsDepartExpected;
	}

	public String getTmsRemarkDispatch() {
		return tmsRemarkDispatch;
	}

	public void setTmsRemarkDispatch(String tmsRemarkDispatch) {
		this.tmsRemarkDispatch = tmsRemarkDispatch;
	}

	public Date getTmsDateLoad() {
		return tmsDateLoad;
	}

	public void setTmsDateLoad(Date tmsDateLoad) {
		this.tmsDateLoad = tmsDateLoad;
	}

	public Date getTmsDateConfirmed() {
		return tmsDateConfirmed;
	}

	public void setTmsDateConfirmed(Date tmsDateConfirmed) {
		this.tmsDateConfirmed = tmsDateConfirmed;
	}

	public Date getTmsDateIssue() {
		return tmsDateIssue;
	}

	public void setTmsDateIssue(Date tmsDateIssue) {
		this.tmsDateIssue = tmsDateIssue;
	}

	public String getTmsShipmentNo() {
		return tmsShipmentNo;
	}

	public void setTmsShipmentNo(String tmsShipmentNo) {
		this.tmsShipmentNo = tmsShipmentNo;
	}

	public Short getTmsDropPoint() {
		return tmsDropPoint;
	}

	public void setTmsDropPoint(Short tmsDropPoint) {
		this.tmsDropPoint = tmsDropPoint;
	}

	public String getTmsWearhouseNumber() {
		return tmsWearhouseNumber;
	}

	public void setTmsWearhouseNumber(String tmsWearhouseNumber) {
		this.tmsWearhouseNumber = tmsWearhouseNumber;
	}

	public String getTmsTypeTransport() {
		return tmsTypeTransport;
	}

	public void setTmsTypeTransport(String tmsTypeTransport) {
		this.tmsTypeTransport = tmsTypeTransport;
	}

	public String getTmsTypeConsign() {
		return tmsTypeConsign;
	}

	public void setTmsTypeConsign(String tmsTypeConsign) {
		this.tmsTypeConsign = tmsTypeConsign;
	}

	public Long getTmsFleetIdx() {
		return tmsFleetIdx;
	}

	public void setTmsFleetIdx(Long tmsFleetIdx) {
		this.tmsFleetIdx = tmsFleetIdx;
	}

	public String getTmsFleetName() {
		return tmsFleetName;
	}

	public void setTmsFleetName(String tmsFleetName) {
		this.tmsFleetName = tmsFleetName;
	}

	public Long getTmsVehicleIdx() {
		return tmsVehicleIdx;
	}

	public void setTmsVehicleIdx(Long tmsVehicleIdx) {
		this.tmsVehicleIdx = tmsVehicleIdx;
	}

	public String getTmsPlateNumber() {
		return tmsPlateNumber;
	}

	public void setTmsPlateNumber(String tmsPlateNumber) {
		this.tmsPlateNumber = tmsPlateNumber;
	}

	public String getTmsVehicleType() {
		return tmsVehicleType;
	}

	public void setTmsVehicleType(String tmsVehicleType) {
		this.tmsVehicleType = tmsVehicleType;
	}

	public String getTmsVehicleSize() {
		return tmsVehicleSize;
	}

	public void setTmsVehicleSize(String tmsVehicleSize) {
		this.tmsVehicleSize = tmsVehicleSize;
	}

	public Long getTmsDriverIdx() {
		return tmsDriverIdx;
	}

	public void setTmsDriverIdx(Long tmsDriverIdx) {
		this.tmsDriverIdx = tmsDriverIdx;
	}

	public String getTmsDriverName() {
		return tmsDriverName;
	}

	public void setTmsDriverName(String tmsDriverName) {
		this.tmsDriverName = tmsDriverName;
	}

	public String getTmsDriverTel() {
		return tmsDriverTel;
	}

	public void setTmsDriverTel(String tmsDriverTel) {
		this.tmsDriverTel = tmsDriverTel;
	}

	public String getOtsRemarkDelivery() {
		return otsRemarkDelivery;
	}

	public void setOtsRemarkDelivery(String otsRemarkDelivery) {
		this.otsRemarkDelivery = otsRemarkDelivery;
	}

	public String getOtsRemarkReturn() {
		return otsRemarkReturn;
	}

	public void setOtsRemarkReturn(String otsRemarkReturn) {
		this.otsRemarkReturn = otsRemarkReturn;
	}

	public Date getOtsDeliveryNotice() {
		return otsDeliveryNotice;
	}

	public void setOtsDeliveryNotice(Date otsDeliveryNotice) {
		this.otsDeliveryNotice = otsDeliveryNotice;
	}

	public Date getOtsDeliveryActual() {
		return otsDeliveryActual;
	}

	public void setOtsDeliveryActual(Date otsDeliveryActual) {
		this.otsDeliveryActual = otsDeliveryActual;
	}

	public Date getOtsReturnDate() {
		return otsReturnDate;
	}

	public void setOtsReturnDate(Date otsReturnDate) {
		this.otsReturnDate = otsReturnDate;
	}

	public Long getOtsDaysKpi() {
		return otsDaysKpi;
	}

	public void setOtsDaysKpi(Long otsDaysKpi) {
		this.otsDaysKpi = otsDaysKpi;
	}

	public Long getOtsDaysReturn() {
		return otsDaysReturn;
	}

	public void setOtsDaysReturn(Long otsDaysReturn) {
		this.otsDaysReturn = otsDaysReturn;
	}

	public String getOtsReasonDelivery() {
		return otsReasonDelivery;
	}

	public void setOtsReasonDelivery(String otsReasonDelivery) {
		this.otsReasonDelivery = otsReasonDelivery;
	}

	public String getOtsReasonReturn() {
		return otsReasonReturn;
	}

	public void setOtsReasonReturn(String otsReasonReturn) {
		this.otsReasonReturn = otsReasonReturn;
	}

	public String getOtsReasonPart() {
		return otsReasonPart;
	}

	public void setOtsReasonPart(String otsReasonPart) {
		this.otsReasonPart = otsReasonPart;
	}

	public String getOtsDeliveryQualified() {
		return otsDeliveryQualified;
	}

	public void setOtsDeliveryQualified(String otsDeliveryQualified) {
		this.otsDeliveryQualified = otsDeliveryQualified;
	}

	public String getOtsDeliveryComplete() {
		return otsDeliveryComplete;
	}

	public void setOtsDeliveryComplete(String otsDeliveryComplete) {
		this.otsDeliveryComplete = otsDeliveryComplete;
	}

	public String getOtsReturnQualified() {
		return otsReturnQualified;
	}

	public void setOtsReturnQualified(String otsReturnQualified) {
		this.otsReturnQualified = otsReturnQualified;
	}

	public String getBmsFlag() {
		return bmsFlag;
	}

	public void setBmsFlag(String bmsFlag) {
		this.bmsFlag = bmsFlag;
	}

	public String getReference01() {
		return reference01;
	}

	public void setReference01(String reference01) {
		this.reference01 = reference01;
	}

	public String getReference02() {
		return reference02;
	}

	public void setReference02(String reference02) {
		this.reference02 = reference02;
	}

	public Date getReference03() {
		return reference03;
	}

	public void setReference03(Date reference03) {
		this.reference03 = reference03;
	}

	public String getReference04() {
		return reference04;
	}

	public void setReference04(String reference04) {
		this.reference04 = reference04;
	}

	public String getReference05() {
		return reference05;
	}

	public void setReference05(String reference05) {
		this.reference05 = reference05;
	}

	public Date getReference06() {
		return reference06;
	}

	public void setReference06(Date reference06) {
		this.reference06 = reference06;
	}

	public String getReference07() {
		return reference07;
	}

	public void setReference07(String reference07) {
		this.reference07 = reference07;
	}

	public String getReference08() {
		return reference08;
	}

	public void setReference08(String reference08) {
		this.reference08 = reference08;
	}

	public String getReference09() {
		return reference09;
	}

	public void setReference09(String reference09) {
		this.reference09 = reference09;
	}

	public String getReference10() {
		return reference10;
	}

	public void setReference10(String reference10) {
		this.reference10 = reference10;
	}

	public String getUpdate01() {
		return update01;
	}

	public void setUpdate01(String update01) {
		this.update01 = update01;
	}

	public String getUpdate02() {
		return update02;
	}

	public void setUpdate02(String update02) {
		this.update02 = update02;
	}

	public String getUpdate03() {
		return update03;
	}

	public void setUpdate03(String update03) {
		this.update03 = update03;
	}

	public String getUpdate04() {
		return update04;
	}

	public void setUpdate04(String update04) {
		this.update04 = update04;
	}

	public String getUpdate05() {
		return update05;
	}

	public void setUpdate05(String update05) {
		this.update05 = update05;
	}

	public String getUpdate06() {
		return update06;
	}

	public void setUpdate06(String update06) {
		this.update06 = update06;
	}

	public Double getOrdPalletQty() {
		return ordPalletQty;
	}

	public void setOrdPalletQty(Double ordPalletQty) {
		this.ordPalletQty = ordPalletQty;
	}

}
