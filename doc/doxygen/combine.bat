set destpath=..\all.tmp\touchvg

del /Q/S  %destpath%

xcopy ..\..\core\pub_inc\*.* %destpath%\ /Y/S
xcopy ..\..\core\mgr_inc\*.* %destpath%\ /Y/S

xcopy ..\..\core\pub_src\geom\*.* %destpath%\geom_src\ /Y
xcopy ..\..\core\pub_src\graph\*.* %destpath%\graph_src\ /Y
xcopy ..\..\core\pub_src\shape\*.* %destpath%\shape_src\ /Y
xcopy ..\..\core\pub_src\cmdbase\*.* %destpath%\cmdbase_src\ /Y

xcopy ..\..\core\mgr_src\shapedoc\*.* %destpath%\shapedoc_src\ /Y
xcopy ..\..\core\mgr_src\cmdbasic\*.* %destpath%\cmdbasic_src\ /Y
xcopy ..\..\core\mgr_src\cmdmgr\*.* %destpath%\cmdmgr_src\ /Y
xcopy ..\..\core\mgr_src\json\*.* %destpath%\json_src\ /Y
xcopy ..\..\core\mgr_src\export\*.* %destpath%\export_src\ /Y
xcopy ..\..\core\mgr_src\record\*.* %destpath%\record_src\ /Y
xcopy ..\..\core\mgr_src\view\*.* %destpath%\view_src\ /Y

xcopy ..\..\core\democmds\gate\*.* %destpath%\democmds\ /Y
xcopy ..\..\core\democmds\cmds\*.* %destpath%\democmds\ /Y

xcopy ..\..\win32\include\canvas\*.* %destpath%\winview\ /Y
xcopy ..\..\win32\src\canvas\*.* %destpath%\winview_src\ /Y
xcopy ..\..\win32\include\view\*.* %destpath%\winview\ /Y
xcopy ..\..\win32\mgr_src\view\*.* %destpath%\winview_src\ /Y

xcopy ..\..\ios\view\include\*.* %destpath%\iosview\ /Y
xcopy ..\..\ios\view\src\*.* %destpath%\iosview_src\ /Y

xcopy ..\..\android\core\src\touchvg\view\*.java %destpath%\android\ /Y
xcopy ..\..\wpf\touchvglib\view\*.cs %destpath%\wpf\ /Y
