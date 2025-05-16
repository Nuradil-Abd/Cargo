Swagger: http://localhost:8088/api/opc/swagger-ui/index.html


OPC Encryption Service (Mastercard & Visa)

This service provides backend-side generation of OPC data for Mastercard and Visa, replacing direct SIC calls from mobile apps.

	•	Unified REST API for generating OPC payloads.
	•	Supports:
	•	Visa (VTS Push Provisioning)
	•	Mastercard (MDES FundingAccountInfo + TAV)
	•	Returns only two fields (as expected by frontend): 
 
{
  "result_code": "00",
  "result_data": "{ ... encrypted payload ... }"
}

	•	Card type detected by PAN prefix (4 = Visa, 5 = Mastercard).


Mastercard (MDES)
	•	AES encryption of card data.
	•	RSA encryption of AES key using public_key_mc.pem.
	•	TAV (TokenizationAuthenticationValue) generated using issuer keys:
	•	private_key.der, public_key.der
	•	Payload includes:
	•	encryptedKey
	•	encryptedData
	•	iv
	•	publicKeyFingerprint
	•	(optional) TAV

⸻

  Visa (VTS)
	•	Payload built from frontend fields:
	•	accountNumber, provider, billingAddress, expirationDate
	•	Encrypted into JWE format using Visa specs.




