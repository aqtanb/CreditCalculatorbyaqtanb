package com.aktanberdi.creditcalculatorbyaqtanb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import com.aktanberdi.creditcalculatorbyaqtanb.ui.theme.CreditCalculatorByAqtanbTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreditCalculatorByAqtanbTheme {
                Surface (
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreditCalculatorApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCalculatorApp() {

    var creditAmount by remember { mutableStateOf("") }
    var loanTerm by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }

    val creditAmountDouble = creditAmount.toDoubleOrNull() ?: 0.0
    val loanTermInt = loanTerm.toIntOrNull() ?: 0
    val interestRateDouble = interestRate.toDoubleOrNull() ?: 0.0

    var expanded by remember { mutableStateOf(false) }
    var selectedCreditType by remember { mutableStateOf("Annuity") }
    val creditTypes = listOf("Annuity", "Differential", "Installment")

    val credit: Credit? = when (selectedCreditType) {
        "Annuity" -> AnnuityCredit(creditAmountDouble, interestRateDouble, loanTermInt)
        "Differential" -> DifferentiatedCredit(creditAmountDouble, interestRateDouble, loanTermInt)
        "Installment" -> InstallmentCredit(creditAmountDouble, loanTermInt)
        else -> null
    }

    val payment = credit?.calculatePayment() ?: 0.0
    val overpayment = credit?.calculateOverpayment() ?: 0.0
    val totalAmount = credit?.calculateTotalAmount() ?: 0.0

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculator_title),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.credit_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = creditAmount,
            onValueChanged = { creditAmount = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        EditNumberField(
            label = R.string.loan_term,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = loanTerm,
            onValueChanged = { loanTerm = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )
        EditNumberField(
            label = R.string.rate,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = interestRate,
            onValueChanged = { interestRate = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedCreditType,
                onValueChange = { },
                label ={ Text("Credit Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                creditTypes.forEach { creditType ->
                    DropdownMenuItem(onClick = {
                        selectedCreditType = creditType
                        expanded = false
                    }, text = { Text(creditType) })
                }
            }
        }

        Spacer(modifier = Modifier.height(72.dp))

        val formattedPayment = NumberFormat.getCurrencyInstance().format(payment)
        val formattedOverpayment = NumberFormat.getCurrencyInstance().format(overpayment)
        val formattedTotalAmount = NumberFormat.getCurrencyInstance().format(totalAmount)

        Text(
            text = stringResource(R.string.payment, formattedPayment),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.overpayment, formattedOverpayment),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.total_amount, formattedTotalAmount),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreditCalculatorAppPreview() {
    CreditCalculatorByAqtanbTheme {
        CreditCalculatorApp()
    }
}