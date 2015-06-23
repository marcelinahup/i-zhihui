var fs  = require('fs'); 
var jsp = require("./uglify-js").parser;
var pro = require("./uglify-js").uglify;

/* 读取一个文件，并压缩 */
function buildOne(flieIn, fileOut) {
	var origCode = fs.readFileSync(flieIn, 'utf8');
	var ast = jsp.parse(origCode); 
		ast = pro.ast_mangle(ast); 
		ast = pro.ast_squeeze(ast);
		
	var finalCode = pro.gen_code(ast);
	
	fs.writeFileSync(fileOut, finalCode, 'utf8');
}

/* 扫描目录，压缩文件 */
function scanFolderCompress(path,dist){

    console.log('=========================压缩开发==========================');
    console.log('');
    console.log('===-- 扫描' + path +'，压缩文件 --===');
    console.log('');

    var fileList = [],
        folderList = [],
        srcPath = path,
        walk = function(srcPath, fileList, folderList){
            files = fs.readdirSync(srcPath);
            files.forEach(function(item) {  
                var tmpPath = srcPath + '/' + item,
                    stats = fs.statSync(tmpPath);

                if (stats.isDirectory()) {  
                
                    folderList.push(tmpPath); 

                    // 去除指定的目录
                    var distPath = tmpPath.replace(path,dist);
                    
                    if (!fs.existsSync(distPath)) {
                        fs.mkdirSync(distPath);
                        console.log('成功创建目录' + distPath);
                    }
                                     
                    walk(tmpPath, fileList, folderList); 
  
                } else {  
                    fileList.push(tmpPath); 

                    console.log('扫描到文件：' + tmpPath);

                    // 去除指定的目录
                    var distPath = tmpPath.replace(path,dist);

                    // 生成带后缀min.js的压缩文件
                    var distFile = distPath.substring(0,distPath.lastIndexOf('.js')) + '.min.js';
                    console.log('压缩生成指定文件：' + distFile);

                    // 压缩文件
                    buildOne(tmpPath,distFile);
                }  
            });  
        };  


    walk(srcPath, fileList, folderList);
    console.log('');
    console.log('=========================压缩完成==========================');
}

// 目标目录要先创建
scanFolderCompress('C:/myApp/scripts','c:/myApp/script2');
