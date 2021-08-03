<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="contextPath" content="${pageContext.request.contextPath}" />
<link rel="shortcut icon" href="./img/Logo.png">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
	integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS"
	crossorigin="anonymous">
	
<link rel="stylesheet" type="text/css" href="./css/index.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<body>

	<div class="backgroundImage">
		<div class="header">
			<a href="" class="logo"><img src="./img/Logo.png" height="90px"></a>
			<div class="header-right">
				<h1>Om's Development Center</h1>
			</div>
			<div style="align-items: flex-end; width: 6%;">
				<button onclick="location.href='EnquiryLogin.jsp'" type="button"
					class="btn btn-danger btn-block">Log Out</button>
			</div>
		</div>
		<div class="container">
			<div class="container" align="center">
				<div>
					<nav class="navbar navbar navbar-dark bg-dark">
						<h3
							style="color: white; font-weight: 900; width: 100%; align-items: center;">
							<b>X-WorkZ Enquiry And Call Management</b>
						</h3>
					</nav>
				</div>
				<div class="row"
					style="color: white; text-align: center; margin-top: 2%">
					<div class="col-md-3"></div>
					<div class="col-md-6">
						<h3 style="color: red;">
							<b>${LoginMsg}${success}${faild}${msg}</b>
						</h3>
					</div>
					<div class="col-md-3"></div>
				</div>
			</div>
			<div class="container container_border ">
				<div class="">
					<h2 align="center"
						style="margin-top: 5%; color: white; padding-top: 3%">Enquiry
						Management</h2>
					<div class="panel panel-default">
						<div class="panel-body" align="center" style="margin-top: 2%">
							<div class="row mt-3 mb-3">
								<div class="col-sm-4"></div>
								<div class="col-sm-4" align="center">
									<select class="custom-select custom-select-lg sm-3"
										onchange="getEnquiryType()" id="enquiryOperations"
										style="font-size: 20px; text-align: center;">
										<option value="0">Select Enquiry Operation</option>
										<option value="1">New Enquiry</option>
										<option value="2">Upload Enquires</option>
										<option value="3">Download Enquires</option>
										<option value="4">View Enquires</option>
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>

				<script type="text/javascript">
					function handleSelect(page) {
						window.location = page.value + ".jsp";
					}
				</script>
				<br>
				<div class="">
					<h2 align="center"
						style="margin-top: 5%; color: white; padding-top: 3%">Call
						Management</h2>
					<div class="panel panel-default">
						<div class="panel-body" align="center" style="margin-top: 2%">
							<div class="row mt-3 mb-3">
								<div class="col-sm-4"></div>
								<div class="col-sm-4" align="center">
									<select class="custom-select custom-select-lg sm-3"
										onchange="javascript:handleSelect(this)" id="selectOperation"
										style="font-size: 20px; text-align: center;">
										<option value="Welcome">Select Call Operation</option>
										<option value="Login">New Calls</option>
										<option value="EnquiryLogin">Pending Calls</option>
										<option value="EnquiryLogin">Upload Calls</option>
										<option value="EnquiryLogin">View All Calls</option>
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>

				<form name="newenq" class="newEnquiry" method="POST"
					action="newEnquiry.do" style="margin-top: 8%;">
					<table class="col-md-6 table table-bordered table-dark" border="1"
						border-color="white" align="center" style="color: white">
						<tr>
							<td colspan="2" align="center"><h3>New Enquiry</h3></td>
						</tr>

						<tr>
							<td><h5>
									Full Name<sup>*</sup>:
								</h5></td>
							<td><input type="text" class="form-control" name="fullName"
								required placeholder="Enter full name" onblur="checkNameExist()" /><b><span
									id="isName" style="color: red;"></span></b></td>
						</tr>

						<tr>
							<td><h5>
									MOBILE NO<sup>*</sup>:
								</h5></td>
							<td><input type="number" class="form-control"
								name="mobileNo" required placeholder="Enter valid mobile number" onblur="checkMobileNoExist()" /><b><span
									id="isMobile" style="color: red;"></span></b></td>
						</tr>

						<tr>
							<td><h5>
									ALTERNATE MOBILE<sup></sup>:
								</h5></td>
							<td><input type="number" class="form-control"
								name="alternateMobileNo"
								placeholder="Enter alternate mobile number" /></td>
						</tr>

						<tr>
							<td><h5>
									EMAIL ID<sup>*</sup>:
								</h5></td>
							<td><input type="email" class="form-control" name="emailId"
								required placeholder="Enter valid email id"
								onblur="checkEmailExist()" /><b><span id="isE" style="color: red;"></span></b></td>
						</tr>

						<tr>
							<td><h5>
									COURSE<sup>*</sup>:
								</h5></td>
							<td><input type="text" class="form-control" name="course"
								required placeholder="Enter course name" /></td>
						</tr>

						<tr>
							<td><h5>
									BATCH TYPE<sup>*</sup>:
								</h5></td>
							<td><select name="batchType"
								class="custom-select custom-select-lg sm-3">
									<option value="Weekend" label="Weekend" />
									<option value="WeekDay" label="WeekDay" />
							</select></td>
						</tr>

						<tr>
							<td><h5>
									SOURCE<sup>*</sup>:
								</h5></td>
							<td><input type="text" class="form-control" required
								name="source" placeholder="Enter source of information" /></td>
						</tr>

						<tr>
							<td><h5>
									REFRENCE<sup>*</sup>:
								</h5></td>
							<td><input type="text" class="form-control"
								name="refrenceName" placeholder="Enter refrence name" /></td>
						</tr>

						<tr>
							<td><h5>
									REFRENCE MOBILE NO<sup></sup>:
								</h5></td>
							<td><input type="number" class="form-control"
								name="refrenceMobileNo"
								placeholder="Enter refrence mobile number" /></td>
						</tr>

						<tr>
							<td><h5>
									BRANCH TYPE<sup>*</sup>:
								</h5></td>
							<td><select name="branch" required
								class="custom-select custom-select-lg sm-3">
									<option value="Rajajinagar" label="Rajajinagar" />
									<option value="BTM" label="BTM" />
							</select></td>
						</tr>

						<tr>
							<td><h5>
									COUNSELOR<sup>*</sup>:
								</h5></td>
							<td><input type="text" class="form-control" name="counselor"
								required placeholder="Enter refrence name" /></td>
						</tr>

						<tr>
							<td><h5>
									COMMENTS<sup></sup>:
								</h5></td>
							<td><textarea rows="10" cols="10"
									style="overflow: auto; resize: none" maxlength="300"
									class="form-control" name="comments"
									placeholder="Enter comments" required></textarea></td>
						</tr>

						<tr>
							<td colspan="2" align="center"><input type="submit"
								class="col-sm-5 btn btn-light" value="Submit"></td>
						</tr>

					</table>
				</form>

				<form action="uploadEnquiry.do" class="uploadEnquiry" method="post"
					enctype="multipart/form-data">
					<table class="col-md-6 table table-bordered table-dark" border="1"
						border-color="white" align="center" style="color: white">
						<tr>
							<td colspan="2" align="center"><h3>
									<b>Upload Enquiry</b>
								</h3></td>
						</tr>
						<tr>
							<td>
								<h5>Upload Excel File:</h5>
							</td>
							<td><input type="file" class="form-control"
								name="uploadFile"></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input
								class="col-sm-5 btn btn-light" type="submit" value="Upload"></td>
						</tr>

					</table>
				</form>

				<form class="getlatestEnquiries" action="getLatestEnquiries.do"
					method="post">
					<table class="col-md-6 table table-bordered table-dark" border="1"
						border-color="white" align="center" style="color: white">
						<tr>
							<td colspan="2" align="center"><h3>
									<b>View Enquiries</b>
								</h3></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input
								class="col-sm-5 btn btn-light" type="submit"
								value="View Enquiries"></td>
						</tr>
					</table>
				</form>

				<form class="getCloudEnquiries" action="downloadEnquirySchedule.do"
					method="get">
					<table class="col-md-6 table table-bordered table-dark" border="1"
						border-color="white" align="center" style="color: white">
						<tr>
							<td colspan="2" align="center"><h3>
									<b>Download Enquiry</b>
								</h3></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input
								class="col-sm-5 btn btn-light" type="submit" value="Download"></td>
						</tr>
					</table>
				</form>

			</div>
		</div>
		<div>
			<nav class="navbar navbar-expand-md navbar-dark bg-dark"></nav>
		</div>
	</div>

	<!-- Latest compiled JavaScript -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"
		integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"
		integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k"
		crossorigin="anonymous"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script type="text/javascript"
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.0/jquery.validate.min.js"></script>
	<script type="text/javascript" src="./js/index.js"></script>
</body>
</html>