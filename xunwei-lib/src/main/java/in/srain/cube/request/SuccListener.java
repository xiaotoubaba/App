package in.srain.cube.request;



public interface SuccListener<T>{
	public void onSuccess(IRequest<?> request, T result);
}