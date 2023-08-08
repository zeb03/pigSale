for i = 1, ARGV[1] do
    local stock = redis.call('get', KEYS[i])
    if stock < ARGV[i + 1] then
        return -1
    end
    stock = stock - ARGV[i + 1]
    redis.call('set', KEYS[i], stock)
end
return 1