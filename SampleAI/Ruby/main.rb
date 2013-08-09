puts "SampleRuby"
while gets
  if $_.strip == "EOS"
    puts "finish"
    STDOUT.flush
  end
end
