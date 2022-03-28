using System;
using Android.App;
using Android.OS;
using Android.Runtime;
using Android.Views;
using AndroidX.AppCompat.Widget;
using AndroidX.AppCompat.App;
using Google.Android.Material.FloatingActionButton;
using Google.Android.Material.Snackbar;
using Google.Android.Material.Button;

using Kotlin.Jvm.Functions;
using Com.Example.Nativelib;
using Android.Widget;

namespace AndroidNativeTest
{

    class Function1Impl<T> : Java.Lang.Object, IFunction1 where T : Java.Lang.Object
    {
        private readonly Action<T> _onInvoked;

        public Function1Impl(Action<T> onInvoked)
        {
            _onInvoked = onInvoked;
        }

        public Java.Lang.Object Invoke(Java.Lang.Object objParameter)
        {
            try
            {
                T parameter = (T)objParameter;
                _onInvoked?.Invoke(parameter);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }

            return null;
        }
    }

    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            ButtonsConfigure();
            InitNativeLib();
            InitViewFields();
        }

        private void ButtonsConfigure()
        {
            var button = FindViewById<Button>(Resource.Id.bOpen);
            button.Click += ButtonOnClick;
        }

        private void InitNativeLib()
        {
            var configuration = new Com.Example.Nativelib.Models.Configuration
            {
                Domain = "https://api.shopstory.live",
                AppId = "shopstory-prod"
            };


            var nativeLib = NativeLib.Init(configuration);

            nativeLib.OnSentData(new Function1Impl<OperationResponse>((response) => {
                var fProductName = FindViewById<TextView>(Resource.Id.productName);
                var fFeedProductId = FindViewById<TextView>(Resource.Id.feedProductId);
                var fFeedProductGroupId = FindViewById<TextView>(Resource.Id.feedProductGroupId);
                var fVendorCode = FindViewById<TextView>(Resource.Id.vendorCode);

                fProductName.Text = response.Product.Name;
                fFeedProductId.Text = response.Product.FeedProductId;
                fFeedProductGroupId.Text = response.Product.FeedProductGroupId;
                fVendorCode.Text = response.Product.VendorCode;
            }));
            nativeLib?.OnError(new Function1Impl<Java.Lang.String>((error) => {
                var context = Application.Context;
                var duration = ToastLength.Long;

                var toast = Toast.MakeText(context, error, duration);
                toast.Show();
            }));
        }

        private void InitViewFields()
        {
            var domain = FindViewById<Google.Android.Material.TextField.TextInputEditText>(Resource.Id.domain);
            var appId = FindViewById<Google.Android.Material.TextField.TextInputEditText>(Resource.Id.appId);

            domain.Text = NativeLib.Config.Domain;
            appId.Text = NativeLib.Config.AppId;
        }

        private void ChangeConfiguration(string domain, string appId)
        {
            var configuration = NativeLib.Config;
            

            if (configuration != null)
            {
                configuration.Domain = domain;
                configuration.AppId = appId;
                NativeLib.SetConfig(configuration);
            }
        }

        private void ButtonOnClick(object sender, EventArgs eventArgs)
        {
            var nativeLib = NativeLib.Instance;

            var domain = FindViewById<Google.Android.Material.TextField.TextInputEditText>(Resource.Id.domain);
            var appId = FindViewById<Google.Android.Material.TextField.TextInputEditText>(Resource.Id.appId);


            ChangeConfiguration(domain.Text, appId.Text);

            nativeLib.Start(this);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
