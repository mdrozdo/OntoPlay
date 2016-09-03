function getFileExtensionByFieldName(filedId){
	var fileFieldValue=  $('#'+filedId).val();
	var extension=fileFieldValue.substr(fileFieldValue.lastIndexOf('.')+1,fileFieldValue.length-1);
	return extension;
}

function isValidExtension(extension,allowedExtensions){
	if(extension.length==0)
		return false;
	if(allowedExtensions.indexOf(extension)!=-1)
		return true;
	return false;
}

