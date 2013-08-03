cd `dirname $0`
echo $0 >> ~/sh.log 
echo `dirname $0` >> ~/sh.log
echo $USER >> ~/sh.log
cd $(cd $(dirname $0) && pwd)
java Main