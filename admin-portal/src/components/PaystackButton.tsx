import { usePaystackPayment } from 'react-paystack'

const config = {
    reference: (new Date()).getTime().toString(),
    email: "user@example.com",
    amount: 20000, // 200 Naira
    publicKey: import.meta.env.VITE_PAYSTACK_PUBLIC_KEY || '',
};

export const PaystackButton = () => {
    const initializePayment = usePaystackPayment(config);

    const onSuccess = (reference: any) => {
        // Implementation for whatever you want to do with reference and after success call.
        console.log(reference);
        alert("Payment Successful! Ref: " + reference.reference)
    };

    const onClose = () => {
        // implementation for  whatever you want to do when the Paystack dialog closed.
        console.log('closed')
    }

    return (
        <button
            onClick={() => {
                initializePayment({ onSuccess, onClose })
            }}
            className="rounded bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
        >
            Test Paystack Payment
        </button>
    );
};
