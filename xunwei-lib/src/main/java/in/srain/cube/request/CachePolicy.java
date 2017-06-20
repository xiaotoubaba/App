package in.srain.cube.request;

public enum CachePolicy{
	ALWAYS, //只要缓存存在，总是使用缓存
	VALID,  //只有缓存没有过期才使用 
	NONE;    //不使用缓存
}