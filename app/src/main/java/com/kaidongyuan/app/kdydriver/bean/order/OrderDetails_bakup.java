package com.kaidongyuan.app.kdydriver.bean.order;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetails_bakup implements java.io.Serializable {

	private String ordNoClient;// 客户编号
	private String ordFromName;// 客户名称
	private String salesChannels;// 销售渠道
	private String partyClass;// 大区
	private String ordFromProvince;// 地区
	private String ordFromCity;// 办事处
	private String businessType;// 订单类型
	private String ordNo;// 订单编号
	private String priceList;// 价目表
	private String pending;// 是否暂挂
	private String ordState;// 订单状态
	private String salesNo;// 销售申请单号
	private String salesStaff;// 销售人员
	private String tmsWearhouseNumber;// 发货仓库CODE
	private String deliveryWarehouse;// 发货仓库
	private String deliveryWarehouseChild;// 发货字库
	private Date ordDateAdd;// 订单日期
	private Date requestDate;// 请求日期
	private Date tmsDateIssue;// 发货日期
	private String addressNo;// 地址编号
	private String ordToProvince;// 省
	private String ordToCity;// 市
	private String ordToRegion;// 区县
	private String addressInfo;// 详细收货地址
	private String smallClassMaterial;// 物料小类
	private String materialDetailedClass;// 物料明细类
	private String productNo;// 物料编码
	private String productName;// 物料名称
	private Double orderQty;// 订单数量
	private Double orderWeight;// 订单吨位
	private Double issueQty;// 发货数量
	private Double issueWeight;// 发货吨位
	private String poUom;// 单位
	private Double noTaxAmount;// 不含税金额
	private Double tax;// 税金
	private Double leviedInTotal;// 价税合计
	private Double promotionPrice;// 促销单价
	private Double price;// 合同单价
	private Double spreads; // 促销价差
	private Double orderAmountSpreads;// 订货价差金额
	private Double deliveryPriceAmount;// 发货价差金额
	private String collectingFreight;// 代收运费
	private Double sum;// 合计
	private String tmsFleetName;// 承运商
	private String remarkSupplier;// 订单备注

	// 以下数据库里面的字段
	private String idx;// 主键

	private String reference01;
	private String reference02;
	private String reference03;
	private String reference04;
	private String reference05;
	private String reference06;
	private String reference07;
	private String reference08;
	private BigDecimal monthpo;
	private BigDecimal yearpo;
	// 基础字段
	private int page;
	private int rows;
	private String sort;
	private String order;
	private String ids;
	private String q;
	private Date timeStart;
	private Date timeEnd;

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

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getReference03() {
		return reference03;
	}

	public String getPartyClass() {
		return partyClass;
	}

	public String getOrdState() {
		return ordState;
	}

	public void setOrdState(String ordState) {
		this.ordState = ordState;
	}

	public String getBusinessType() {
		return businessType;
	}

	public String getOrdNoClient() {
		return ordNoClient;
	}

	public void setOrdNoClient(String ordNoClient) {
		this.ordNoClient = ordNoClient;
	}

	public void setPartyClass(String partyClass) {
		this.partyClass = partyClass;
	}

	public void setReference03(String reference03) {
		this.reference03 = reference03;
	}

	public String getReference04() {
		return reference04;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getOrdFromName() {
		return ordFromName;
	}

	public void setReference04(String reference04) {
		this.reference04 = reference04;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getReference05() {
		return reference05;
	}

	public void setReference05(String reference05) {
		this.reference05 = reference05;
	}

	public String getReference06() {
		return reference06;
	}

	public void setReference06(String reference06) {
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

	public BigDecimal getMonthpo() {
		return monthpo;
	}

	public void setMonthpo(BigDecimal monthpo) {
		this.monthpo = monthpo;
	}

	public BigDecimal getYearpo() {
		return yearpo;
	}

	public void setYearpo(BigDecimal yearpo) {
		this.yearpo = yearpo;
	}

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

	public String getSalesChannels() {
		return salesChannels;
	}

	public void setSalesChannels(String salesChannels) {
		this.salesChannels = salesChannels;
	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}

	public String getSalesNo() {
		return salesNo;
	}

	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	public String getSalesStaff() {
		return salesStaff;
	}

	public void setSalesStaff(String salesStaff) {
		this.salesStaff = salesStaff;
	}

	public String getDeliveryWarehouseChild() {
		return deliveryWarehouseChild;
	}

	public void setDeliveryWarehouseChild(String deliveryWarehouseChild) {
		this.deliveryWarehouseChild = deliveryWarehouseChild;
	}

	public String getAddressNo() {
		return addressNo;
	}

	public void setAddressNo(String addressNo) {
		this.addressNo = addressNo;
	}

	public Double getNoTaxAmount() {
		return noTaxAmount;
	}

	public void setNoTaxAmount(Double noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getLeviedInTotal() {
		return leviedInTotal;
	}

	public void setLeviedInTotal(Double leviedInTotal) {
		this.leviedInTotal = leviedInTotal;
	}

	public Double getPromotionPrice() {
		return promotionPrice;
	}

	public void setPromotionPrice(Double promotionPrice) {
		this.promotionPrice = promotionPrice;
	}

	public Double getSpreads() {
		return spreads;
	}

	public void setSpreads(Double spreads) {
		this.spreads = spreads;
	}

	public Double getOrderAmountSpreads() {
		return orderAmountSpreads;
	}

	public void setOrderAmountSpreads(Double orderAmountSpreads) {
		this.orderAmountSpreads = orderAmountSpreads;
	}

	public Double getDeliveryPriceAmount() {
		return deliveryPriceAmount;
	}

	public void setDeliveryPriceAmount(Double deliveryPriceAmount) {
		this.deliveryPriceAmount = deliveryPriceAmount;
	}

	public String getCollectingFreight() {
		return collectingFreight;
	}

	public void setCollectingFreight(String collectingFreight) {
		this.collectingFreight = collectingFreight;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public String getDeliveryWarehouse() {
		return deliveryWarehouse;
	}

	public void setDeliveryWarehouse(String deliveryWarehouse) {
		this.deliveryWarehouse = deliveryWarehouse;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getSmallClassMaterial() {
		return smallClassMaterial;
	}

	public void setSmallClassMaterial(String smallClassMaterial) {
		this.smallClassMaterial = smallClassMaterial;
	}

	public String getMaterialDetailedClass() {
		return materialDetailedClass;
	}

	public void setMaterialDetailedClass(String materialDetailedClass) {
		this.materialDetailedClass = materialDetailedClass;
	}

	public void setOrdFromName(String ordFromName) {
		this.ordFromName = ordFromName;
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

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}

	public String getTmsWearhouseNumber() {
		return tmsWearhouseNumber;
	}

	public void setTmsWearhouseNumber(String tmsWearhouseNumber) {
		this.tmsWearhouseNumber = tmsWearhouseNumber;
	}

	public Date getTmsDateIssue() {
		return tmsDateIssue;
	}

	public void setTmsDateIssue(Date tmsDateIssue) {
		this.tmsDateIssue = tmsDateIssue;
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

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}

	public Double getOrderWeight() {
		return orderWeight;
	}

	public void setOrderWeight(Double orderWeight) {
		this.orderWeight = orderWeight;
	}

	public Double getIssueQty() {
		return issueQty;
	}

	public void setIssueQty(Double issueQty) {
		this.issueQty = issueQty;
	}

	public Double getIssueWeight() {
		return issueWeight;
	}

	public void setIssueWeight(Double issueWeight) {
		this.issueWeight = issueWeight;
	}

	public String getPoUom() {
		return poUom;
	}

	public void setPoUom(String poUom) {
		this.poUom = poUom;
	}

	public String getTmsFleetName() {
		return tmsFleetName;
	}

	public void setTmsFleetName(String tmsFleetName) {
		this.tmsFleetName = tmsFleetName;
	}

	public String getRemarkSupplier() {
		return remarkSupplier;
	}

	public void setRemarkSupplier(String remarkSupplier) {
		this.remarkSupplier = remarkSupplier;
	}

	public String getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}

	public Date getOrdDateAdd() {
		return ordDateAdd;
	}

	public void setOrdDateAdd(Date ordDateAdd) {
		this.ordDateAdd = ordDateAdd;
	}
}
