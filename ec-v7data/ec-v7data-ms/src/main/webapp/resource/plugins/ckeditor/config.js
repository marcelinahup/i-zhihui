/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.language = 'zh-cn';
	config.image_previewText=' '; //预览区域显示内容
	config.filebrowserImageUploadUrl= "/sei/base/doUploadImage.html"; // 待会要上传的acton
	
	config.toolbarCanCollapse = true;
	//自定义工具栏 

	  config.toolbar = 'Define'; 

	    config.toolbar_Define = [

		 ['Source', '-', 'Print', 'Image'],
		
		 ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord'],
		
		 ['Bold', 'Italic', 'Underline', 'Strike'],
		
		 ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
		
		 ['Link', 'Unlink', 'Anchor'],
		
		 ['Styles', 'Format', 'Font', 'FontSize', 'TextColor', 'BGColor']
		
		];
};