@echo off


:: 设置缓存文件夹所在位置，注意最后要有两个反斜杠，需要制定在缓存文件夹内，\\前为文件夹名称。
set EVE_CACHEFOLDER=C:\Users\18883\EVE_CACHE\SharedCache\\


:: start C:\Users\18883\Desktop\Release-20220602a\zhpatch-auto.exe



:: 设置eve.exe所在位置，注意不要加引号，即使路径中含有空格
set EVE_EXE_PATH=C:\Program Files\EVE\Launcher\evelauncher.exe


reg add hkcu\software\ccp\eveonline /v CACHEFOLDER /t reg_sz /d "%EVE_CACHEFOLDER%" /f


:: 启动eve
start "" "%EVE_EXE_PATH%"


:: 启动监控
start "" eve_monitor.bat

echo "%EVE_EXE_PATH%"已启动。
echo 此窗口将在3秒后自动关闭。
ping -n 2 127.0.0.1>nul
echo 此窗口将在2秒后自动关闭。
ping -n 2 127.0.0.1>nul
echo 此窗口将在1秒后自动关闭。
ping -n 2 127.0.0.1>nul
echo 此窗口即将关闭。

