package com.vivonet.aws.lambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class LambdaFunctionHandler implements RequestStreamHandler
{
	LambdaLogger logger;

	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		this.logger = context.getLogger();
		JSONObject responseJson = new JSONObject();
		try {
			
			List<Map<String, Object> > listItems = new LinkedList<>();
	        Map<String, Object> emp1 = new HashMap<String, Object>();
	        Map<String, Object> emp2 = new HashMap<String, Object>();
	        Map<String, Object> emp3 = new HashMap<String, Object>();
	        Map<String, Object> emp4 = new HashMap<String, Object>();
	        Map<String, Object> emp5 = new HashMap<String, Object>();
	        
	        emp1.put("employee_id", "1" );
	        emp1.put("name", "Emp1");
	        emp1.put("salary", new BigDecimal("14135.50") );
	        emp1.put("sales_count", new BigDecimal("216") );
	        listItems.add(emp1);
	        
	        emp2.put("employee_id", "2" );
	        emp2.put("name", "Emp2");
	        emp2.put("salary", new BigDecimal("15524") );
	        emp2.put("sales_count", new BigDecimal("346") );
	        listItems.add(emp2);
	        
	        emp3.put("employee_id", "3" );
	        emp3.put("name", "Emp3");
	        emp3.put("salary", new BigDecimal("125414") );
	        emp3.put("sales_count", new BigDecimal("1131") );
	        listItems.add(emp3);
	        
	        emp4.put("employee_id", "4" );
	        emp4.put("name", "Emp4");
	        emp4.put("salary", new BigDecimal("362415.50") );
	        emp4.put("sales_count", new BigDecimal("24552") );
	        listItems.add(emp4);
	        
	        emp5.put("employee_id", "5" );
	        emp5.put("name", "Emp5");
	        emp5.put("salary", new BigDecimal("70000000.00") );
	        emp5.put("sales_count", new BigDecimal("65341") );
	        listItems.add(emp5);
			
//			JSONObject queryParameters = extractQueryStringParameters(inputStream);
			
			//This gets the JRXML
//			AmazonS3Consumer s3Consumer = new AmazonS3Consumer(this.logger);
//			s3Consumer.retrieveTemplateFromS3();
			
			//This connects to the Database
//			DatabaseCredentials credentials = new DatabaseCredentials(this.logger);
//			credentials.buildCredentials();
		
//			RDSConnector connector = new RDSConnector(this.logger);
//			Connection connection = connector.connectToRDS(credentials);
			
			//This gets the data
//			List<EmployeeDataBean> beanList = connector.getBeanList(connection, queryParameters);
			
			ReportGenerator reportGenerator = new ReportGenerator(this.logger);
			String encodedReport = reportGenerator.generateBase64EncodedReport(listItems);
			
			buildSuccessfulResponse(encodedReport, responseJson);
		}
		catch (Exception e) {
			this.buildErrorResponse(e.getMessage(), 500, responseJson);
		}
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(responseJson.toString());
		writer.close();
	}

//	public JSONObject extractQueryStringParameters(InputStream inputStream) throws ParseException, IOException {
//		JSONParser parser = new JSONParser();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//		JSONObject queryParameters = null;
//		try {
//			JSONObject event = (JSONObject) parser.parse((Reader) reader);
//			if (event.get("queryStringParameters") != null) {
//				queryParameters = (JSONObject)event.get("queryStringParameters");
//			}
//			return queryParameters;
//		}
//		catch (ParseException e) {
//			logger.log("Error when parsing query string parameters.");
//			throw e;
//		} catch (IOException e) {
//			logger.log("Error extracting query string parameters.");
//			throw e;
//		}
//	}

	@SuppressWarnings("unchecked")
	public void buildSuccessfulResponse(String encodedReport, JSONObject responseJson) {
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/pdf");
		headerJson.put("Accept", "application/pdf");
		headerJson.put("Content-disposition", "attachment; filename=file.pdf");
		responseJson.put("body", encodedReport);
		responseJson.put("statusCode", 200);
		responseJson.put("isBase64Encoded", true);
		responseJson.put("headers", headerJson);
	}

	@SuppressWarnings("unchecked")
	public void buildErrorResponse(String body, int statusCode, JSONObject responseJson) {
		responseJson.put("body", body);
		responseJson.put("statusCode", statusCode);
	}
}