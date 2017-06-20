package in.srain.cube.request;




public interface ErrorListener{
	public void onError(IRequest<?> request, CubeError error);
}