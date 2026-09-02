# 📜 Moldova Customs & Tax Calculation Rules

This document outlines the fiscal regulations and mathematical formulas used by the application for calculating taxes on imported postal and e-commerce items.

---

## 1. Regulatory Context

Under the Customs Code of the Republic of Moldova (*Codul Vamal al Republicii Moldova*), international postal and courier shipments addressed to individuals are subject to specific customs duties, Value-Added Tax (VAT), and customs processing fees depending on the value threshold of the goods.

---

## 2. Calculation Models

### Scenario A: Current Law (In Effect)

#### 1. Parcels $\le$ 150 EUR:
* **Customs Duty**: `0.00 MDL` (Exempt)
* **VAT (20%)**: `0.00 MDL` (Exempt)
* **Customs Procedure Fee**: `0.00 MDL` (Exempt)
* **Total Payable**: `0.00 MDL`

#### 2. Parcels > 150 EUR:
* **Taxable Base**:
  $$\text{Base (MDL)} = \text{Parcel Value (MDL)} + \text{Shipping Cost (MDL)}$$
* **Customs Duty**:
  $$\text{Duty (MDL)} = \text{Base (MDL)} \times \text{Duty Rate}$$
* **Value-Added Tax (VAT 20%)**:
  $$\text{VAT (MDL)} = (\text{Base (MDL)} + \text{Duty (MDL)}) \times 20\%$$
* **Standard Customs Procedure Fee**: `50.00 MDL`
* **Total Payable**:
  $$\text{Total (MDL)} = \text{Duty (MDL)} + \text{VAT (MDL)} + 50.00\text{ MDL}$$

---

### Scenario B: Fiscal Reform (Effective October 1, 2026)

The Government of Moldova approved a fiscal policy reform amending the taxation of international e-commerce parcels (ordered via platforms like Temu, AliExpress, Shein, iHerb, etc.):

#### 1. Parcels $\le$ 150 EUR:
* **Taxable Base**:
  $$\text{Base (MDL)} = \text{Parcel Value (MDL)}$$
  *(Shipping cost is excluded from the VAT base for parcels under 150 EUR)*
* **Customs Duty**: `0.00 MDL` (0% Duty)
* **Value-Added Tax (VAT 20%)**:
  $$\text{VAT (MDL)} = \text{Base (MDL)} \times 20\%$$
* **Fixed Operational Postal Fee**: `12.00 MDL` *(Flat operational charge per parcel)*
* **Total Payable**:
  $$\text{Total (MDL)} = \text{VAT (MDL)} + 12.00\text{ MDL}$$

#### 2. Parcels > 150 EUR:
* Follows the same standard formula as the current law:
  $$\text{Total (MDL)} = \text{Duty (MDL)} + \text{VAT (MDL)} + 50.00\text{ MDL}$$

---

## 3. Standard Duty Rates by Product Category

| Category | Key | Duty Rate |
| :--- | :--- | :--- |
| Mobile Phones | `cat_phones` | **0%** |
| Laptops & PCs | `cat_laptops` | **0%** |
| Toys & Games | `cat_toys` | **0%** |
| Auto Parts | `cat_auto` | **10%** |
| Footwear / Shoes | `cat_shoes` | **10%** |
| Dietary Supplements | `cat_supplements` | **10%** |
| Clothing / Apparel | `cat_clothes` | **15%** |
| Cosmetics & Perfumes | `cat_cosmetics` | **15%** |
| Home Appliances | `cat_appliances` | **15%** |
| Other Goods | `cat_other` | **10%** |
