import sys
print 'SamplePython'
for line in sys.stdin:
  if line == 'EOS':
    print 'finish'

