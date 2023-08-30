package com.vivonet.aws.lambda;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.util.Base64;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportGenerator {

	static final String outFile = "/tmp/Reports.pdf";
	static final String fileName = "/tmp/template.jrxml";

	private LambdaLogger logger;

	public ReportGenerator(LambdaLogger logger) {
		this.logger = logger;
	}

	public String generateBase64EncodedReport(List<Map<String, Object> > beanList) throws JRException, IOException, Exception {
		try {
			File file = new File(outFile);
			OutputStream outputSteam = new FileOutputStream(file);
			generateReport(beanList, outputSteam);
			byte[] encoded = Base64.encode(FileUtils.readFileToByteArray(file));
			return new String(encoded, StandardCharsets.US_ASCII);
		} catch (FileNotFoundException e) {
			logger.log("It was not possible to access the output file: " + e.getMessage());
			throw e;
		} catch (IOException e) {
			logger.log("It was not possible to read and encode the report: " + e.getMessage());
			throw e;
		}
	}

	public void generateReport(List<Map<String, Object> > beanList, OutputStream outputSteam) throws JRException, Exception {
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(beanList);
		Map<String, Object> parameter = new HashMap<String, Object>();
		
		StringBuilder jrxmlContent = ReportGenerator.getJRXML();
		
		parameter.put("title", new String("Report Example"));
		parameter.put("ReportCollectionParam", beanColDataSource);
		JasperReport jasperDesign = null;
		
		ByteArrayInputStream bis = new ByteArrayInputStream(jrxmlContent.toString().getBytes(StandardCharsets.UTF_8 ));
		try {
			jasperDesign = JasperCompileManager.compileReport(bis);
		} catch (JRException e) {
			throw e;
		} finally {
			if(bis != null)
				bis.close();
		}
		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter,
					new JREmptyDataSource());
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputSteam);
		} catch (JRException e) {
			logger.log("There was an error while generating the report: " + e.getMessage());
			throw e;
		}
	}
	
	private static StringBuilder getJRXML() {
		StringBuilder f = new StringBuilder("");
		
		f.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<!-- Created with Jaspersoft Studio version 6.19.1.final using JasperReports Library version 6.19.1-867c00bf88cd4d784d404379d6c05e1b419e8a4c  -->\r\n"
				+ "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"Initial1\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"d712db3c-50f2-463a-82a8-2bd3e22c8197\">\r\n"
				+ "	<style name=\"Style1\" hTextAlign=\"Center\" fontName=\"SansSerif\" isBold=\"true\"/>\r\n"
				+ "	<style name=\"Table_TH\" mode=\"Opaque\" backcolor=\"#F0F8FF\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table_CH\" mode=\"Opaque\" backcolor=\"#BFE1FF\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<style name=\"Table_TD\" mode=\"Opaque\" backcolor=\"#FFFFFF\">\r\n"
				+ "		<box>\r\n"
				+ "			<pen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<topPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<leftPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<bottomPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "			<rightPen lineWidth=\"0.5\" lineColor=\"#000000\"/>\r\n"
				+ "		</box>\r\n"
				+ "	</style>\r\n"
				+ "	<subDataset name=\"Dataset1\" uuid=\"fe77a0e7-5e4b-4ca8-9fc3-4e5e9c83d7f8\">\r\n"
				+ "		<queryString>\r\n"
				+ "			<![CDATA[]]>\r\n"
				+ "		</queryString>\r\n"
				+ "		<field name=\"employee_id\" class=\"java.lang.String\"/>\r\n"
				+ "		<field name=\"name\" class=\"java.lang.String\"/>\r\n"
				+ "		<field name=\"sales_count\" class=\"java.math.BigDecimal\"/>\r\n"
				+ "		<field name=\"salary\" class=\"java.math.BigDecimal\"/>\r\n"
				+ "		<variable name=\"sum_sales_count\" class=\"java.math.BigDecimal\" calculation=\"Sum\">\r\n"
				+ "			<variableExpression><![CDATA[$F{sales_count}]]></variableExpression>\r\n"
				+ "		</variable>\r\n"
				+ "		<variable name=\"avg_salary\" class=\"java.math.BigDecimal\" calculation=\"Average\">\r\n"
				+ "			<variableExpression><![CDATA[$F{salary}]]></variableExpression>\r\n"
				+ "		</variable>\r\n"
				+ "	</subDataset>\r\n"
				+ "	<parameter name=\"title\" class=\"java.lang.String\"/>\r\n"
				+ "	<parameter name=\"ReportCollectionParam\" class=\"net.sf.jasperreports.engine.data.JRBeanCollectionDataSource\"/>\r\n"
				+ "	<queryString>\r\n"
				+ "		<![CDATA[]]>\r\n"
				+ "	</queryString>\r\n"
				+ "	<background>\r\n"
				+ "		<band splitType=\"Stretch\"/>\r\n"
				+ "	</background>\r\n"
				+ "	<title>\r\n"
				+ "		<band height=\"79\" splitType=\"Stretch\">\r\n"
				+ "			<textField>\r\n"
				+ "				<reportElement style=\"Style1\" x=\"0\" y=\"24\" width=\"555\" height=\"30\" uuid=\"9bf278a4-6ae5-4026-8fed-a47daae8c2c8\">\r\n"
				+ "					<property name=\"com.jaspersoft.studio.unit.width\" value=\"px\"/>\r\n"
				+ "				</reportElement>\r\n"
				+ "				<textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\r\n"
				+ "					<font size=\"16\"/>\r\n"
				+ "				</textElement>\r\n"
				+ "				<textFieldExpression><![CDATA[$P{title} + \": Welcome\"]]></textFieldExpression>\r\n"
				+ "			</textField>\r\n"
				+ "		</band>\r\n"
				+ "	</title>\r\n"
				+ "	<detail>\r\n"
				+ "		<band height=\"89\" splitType=\"Stretch\">\r\n"
				+ "			<componentElement>\r\n"
				+ "				<reportElement x=\"48\" y=\"0\" width=\"459\" height=\"89\" uuid=\"69c3b7eb-d0f8-4369-b3fa-3010ddaffe27\">\r\n"
				+ "					<property name=\"com.jaspersoft.studio.layout\" value=\"com.jaspersoft.studio.editor.layout.VerticalRowLayout\"/>\r\n"
				+ "					<property name=\"com.jaspersoft.studio.table.style.table_header\" value=\"Table_TH\"/>\r\n"
				+ "					<property name=\"com.jaspersoft.studio.table.style.column_header\" value=\"Table_CH\"/>\r\n"
				+ "					<property name=\"com.jaspersoft.studio.table.style.detail\" value=\"Table_TD\"/>\r\n"
				+ "					<property name=\"com.jaspersoft.studio.unit.width\" value=\"px\"/>\r\n"
				+ "				</reportElement>\r\n"
				+ "				<jr:table xmlns:jr=\"http://jasperreports.sourceforge.net/jasperreports/components\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd\">\r\n"
				+ "					<datasetRun subDataset=\"Dataset1\" uuid=\"f28186c6-5f38-4e4a-b4d6-7e4afbefe6aa\">\r\n"
				+ "						<dataSourceExpression><![CDATA[$P{ReportCollectionParam}]]></dataSourceExpression>\r\n"
				+ "					</datasetRun>\r\n"
				+ "					<jr:column width=\"115\" uuid=\"4a5abd7b-e294-49c8-8caa-77e4e38fd9a6\">\r\n"
				+ "						<jr:tableFooter style=\"Table_TH\" height=\"30\"/>\r\n"
				+ "						<jr:columnHeader style=\"Table_CH\" height=\"30\">\r\n"
				+ "							<property name=\"com.jaspersoft.studio.unit.width\" value=\"pixel\"/>\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"b5670901-9395-4a99-a146-d3be3c6172e4\"/>\r\n"
				+ "								<text><![CDATA[Employee ID]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table_TD\" height=\"30\">\r\n"
				+ "							<textField>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"6c2640da-b825-4667-8270-7fec8fdba588\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{employee_id}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "					<jr:column width=\"115\" uuid=\"f7a19e77-0d5a-49d0-a858-313e256ad98f\">\r\n"
				+ "						<jr:tableFooter style=\"Table_TH\" height=\"30\"/>\r\n"
				+ "						<jr:columnHeader style=\"Table_CH\" height=\"30\">\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"f4361b9a-96d8-4e0d-a1d7-09ed4366f0b8\"/>\r\n"
				+ "								<text><![CDATA[Name]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table_TD\" height=\"30\">\r\n"
				+ "							<textField>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"cdde364a-72ad-4b02-895c-069f04efd19c\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{name}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "					<jr:column width=\"115\" uuid=\"4ff6268c-c0d4-4a4f-af98-aed4398664d2\">\r\n"
				+ "						<jr:tableFooter style=\"Table_TH\" height=\"30\">\r\n"
				+ "							<textField pattern=\"#,##0;(#,##0)\">\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"6bae66f0-45f9-47bb-82d9-c4b329f7d595\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$V{sum_sales_count}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:tableFooter>\r\n"
				+ "						<jr:columnHeader style=\"Table_CH\" height=\"30\">\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"e7d831b8-f73b-4df8-838a-811bb8ec64a3\"/>\r\n"
				+ "								<text><![CDATA[Sales Count]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table_TD\" height=\"30\">\r\n"
				+ "							<textField pattern=\"#,##0;(#,##0)\">\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"5adb086d-c77f-411e-82dd-ca42dfa5b252\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{sales_count}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "					<jr:column width=\"115\" uuid=\"ad890fd4-0911-44f5-80a1-050a8ddfab89\">\r\n"
				+ "						<jr:tableFooter style=\"Table_TH\" height=\"30\">\r\n"
				+ "							<textField pattern=\"#,##0.00;(#,##0.00)\">\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"9ec112a6-818e-452d-9dac-16771fb62217\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$V{avg_salary}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:tableFooter>\r\n"
				+ "						<jr:columnHeader style=\"Table_CH\" height=\"30\">\r\n"
				+ "							<staticText>\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"82486d2c-91a1-4587-9262-25346beabbf7\"/>\r\n"
				+ "								<text><![CDATA[Salary]]></text>\r\n"
				+ "							</staticText>\r\n"
				+ "						</jr:columnHeader>\r\n"
				+ "						<jr:detailCell style=\"Table_TD\" height=\"30\">\r\n"
				+ "							<textField pattern=\"#,##0.00;(#,##0.00)\">\r\n"
				+ "								<reportElement x=\"0\" y=\"0\" width=\"115\" height=\"30\" uuid=\"2922d1ec-af13-46fd-9925-6e8ba0fd370c\"/>\r\n"
				+ "								<textFieldExpression><![CDATA[$F{salary}]]></textFieldExpression>\r\n"
				+ "							</textField>\r\n"
				+ "						</jr:detailCell>\r\n"
				+ "					</jr:column>\r\n"
				+ "				</jr:table>\r\n"
				+ "			</componentElement>\r\n"
				+ "		</band>\r\n"
				+ "	</detail>\r\n"
				+ "</jasperReport>");
		
		return f;
	}
}
