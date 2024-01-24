for i = 1, ARGV[1] do
    local stock = redis.call('get', 'product:stock:' .. KEYS[i])
    if tonumber(stock) < tonumber(ARGV[i + 1]) then
        return tonumber(KEYS[i])
    end
    --stock = stock - ARGV[i + 1]
    --redis.call('set', 'product:stock:' .. KEYS[i], stock)
end
return 0