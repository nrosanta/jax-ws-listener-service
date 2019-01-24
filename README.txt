# WebhookTestListener

	- A POST notification to the listener will write/append the content to a rolling log file (both size-2MB and time based-1Day triggering policies used).
		# It returns a 200 OK status code with status as success in a JSON format.
		# It creates a consolidated log file for the current date rolled over by size (2MB).
		# It creates a JSON file named by the notification_id
		# Both the .log and .json files are cleaned up if the last modified date is greater than 30 days.
		# A POST call to "http://localhost:8082/WebhookTestListener/TestError" will return 500 error by default.
		
	- A GET call to the listener will return the content of the log file in one or more of the options below:
		# Retrieve details by notification_id 
			Example: http://localhost:8082/WebhookTestListener/WebhookTestServlet?notification_id=2475842785782578427584275259
		# Retrieve details by log file name which is timestamped by date created.
			Example: http://localhost:8082/WebhookTestListener/WebhookTestServlet?log_name=webhooks-2018-09-21.log
		# Retrieve list of notifications received. 
			Example: http://localhost:8082/WebhookTestListener/WebhookTestServlet?list_type=notifications
		# Retrieve list of log files captured. 
			Example: http://localhost:8082/WebhookTestListener/WebhookTestServlet?list_type=logs
		# Retrieve entire content of the default log file
			Example: http://localhost:8082/WebhookTestListener/WebhookTestServlet. It will retrieve the data for the current date.
			
	- Sample data logged as ".json" file:
			{
		    "notification_id": "2475842785782578427584275274",
		    "headers": {
		        "content-type": "application/json",
		        "client-request-id": "6b0da668-baea-47a9-9ead-3adf694a5ea7",
		        "authorization": "Bearer 599f9b701719e808c2fc9ec40ae31c75",
		        "cache-control": "no-cache",
		        "postman-token": "758e33eb-a08a-475d-bba2-5d4d3c23cf69",
		        "user-agent": "PostmanRuntime/7.3.0",
		        "accept": "*/*",
		        "host": "localhost:8082",
		        "accept-encoding": "gzip, deflate",
		        "content-length": "608",
		        "connection": "keep-alive"
		    },
		    "payload": {
		        
		    },
		    "response": {
		        "http_response_code": "200",
		        "status": "success"
		    }
		}