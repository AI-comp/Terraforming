cd `dirname $0`
echo $0
echo `dirname $0`
echo $(cd $(dirname $0) && pwd)
cd $(cd $(dirname $0) && pwd)
java Main