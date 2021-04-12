local function decrease_resource(label, decrement)
    return redis.call('DECRBY', label, decrement)
end

local function set_allocation_limit(label, limit)
    redis.call('SET', label, limit)
    return tonumber(redis.call('GET', label))
end

local function get_allocation(label)
    return redis.call('GET', label)
end

local function increase_resource(label, limitLabel, increment)
    local gaugeState = tonumber(redis.call('get', label) or 0)
    local limit = tonumber(redis.call('get', limitLabel) or 0)
    if (limit >= gaugeState + increment) then
        return redis.call('INCRBY', label, increment)
    else
        return -1;
    end
end

local function map_result(result)
    if (result >= 0) then return "200:"..result end
    return "400:"..result
end

local label = KEYS[1]
local command = ARGV[1]
local number = ARGV[2]
local postFix = "_limit"

print(label)
print(KEYS[2])
print(table.getn(KEYS))

if command == "SET_LIMIT"
then
    local result = set_allocation_limit(label..postFix, number)
    return map_result(result)
elseif command == "ADD_RESOURCE"
then
    local ll = label..postFix
    return map_result(increase_resource(label, ll, number))
elseif command == "REMOVE_RESOURCE"
then
    local limit = decrease_resource(label, number)
    if (limit < 0) then reset_allocation(label, 0) end
    local rv = math.max(limit, 0)
    return "200:"..rv
elseif command == "GET_ALLOCATION"
then
    return "200:"..tostring(get_allocation(label))
elseif command == "RESET_ALLOCATION"
then
    local result = set_allocation_limit(label, number)
    return map_result(result)
else return "400:Did not match any parameter" end