find . -name "*.java"|xargs wc -l|grep "total"|awk '{print $1}'  
