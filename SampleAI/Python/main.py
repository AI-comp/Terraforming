import sys

print 'SamplePython'
while True:
    line = sys.stdin.readline()
    if not line:
        break
    if line.strip() == 'EOS':
        print 'finish'
        sys.stdout.flush()