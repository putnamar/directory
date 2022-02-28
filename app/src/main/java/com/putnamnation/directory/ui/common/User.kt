package com.putnamnation.directory.ui.common

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.putnamnation.directory.model.Address
import com.putnamnation.directory.model.Company
import com.putnamnation.directory.model.Location
import com.putnamnation.directory.model.User
import com.putnamnation.directory.ui.theme.AppTheme

@Composable
fun CompanyView(company: Company) {
    Column {
        company.name?.let {
            Text(text = "Company Name: ${company.name}")
        }
        company.catchPhrase?.let {
            Text(text = "Catchphrase: ${company.catchPhrase}")
        }
        company.bs?.let {
            Text(text = "BS: ${company.bs}")
        }
    }
}

@Composable
fun AddressView(
    address: Address, modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        modifier = modifier.testTag("AddressView"),
        contentPadding = PaddingValues(2.dp),
        onClick = onClick
    ) {
        Column() {
            address.street?.let {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = if (address.suite != null) "${address.street} - ${address.suite}" else "${address.street}",
                    textAlign = TextAlign.Center
                )
            }
            address.city?.let {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = if (address.zipcode != null) "${address.city} - ${address.zipcode}" else "${address.city}",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

const val ANIMATION_DURATION = 300

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    initialVisibility: Boolean = false,
    content: @Composable () -> Unit
) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = 300,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(ANIMATION_DURATION))
    }
    AnimatedVisibility(
        visible = visible,
        initiallyVisible = initialVisibility,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .testTag("ExpandableContent")
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
fun OpenView(intent: Intent, text: String?, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    TextButton(
        onClick = {
            context.startActivity(intent)
        }, modifier = modifier
            .padding(0.dp),
        contentPadding = PaddingValues(8.dp, 0.dp)
    ) {
        Text(text = text ?: "", textDecoration = TextDecoration.Underline)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserView(user: User, mapClick: (Location) -> Unit) {
    var expand by remember { mutableStateOf(false) } // Expand State
    val rotationState by animateFloatAsState(if (expand) 180f else 0f) // Rotation State
    var stroke by remember { mutableStateOf(1) }
    val sendToIntent = remember { Intent(Intent.ACTION_SENDTO) }
    val dialIntent = remember { Intent(Intent.ACTION_DIAL) }
    val viewIntent = remember { Intent(Intent.ACTION_VIEW) }

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .testTag("UserView - ${user.id}")
            .padding(8.dp),
        backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(8.dp), // Shape
        border = BorderStroke(stroke.dp, MaterialTheme.colors.onSurface), // Stroke Width and Color
        onClick = {
            expand = !expand
            stroke = if (expand) 2 else 1
        },
        elevation = 8.dp
    ) {
        Column {

            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {

                Text(
                    text = user.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(TopStart)
                        .padding(4.dp)
                        .fillMaxWidth()
                )
                IconButton(
                    modifier = Modifier
                        .rotate(rotationState)
                        .align(TopEnd)
                        .testTag("ExpandIcon")
                        .wrapContentHeight()
                        .wrapContentWidth(),
                    onClick = {
                        expand = !expand
                        stroke = if (expand) 2 else 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Drop Down Arrow - ${user.id}"
                    )
                }
            }
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                OpenView(intent = dialIntent.apply {
                    data = Uri.parse("tel:${user.phone ?: ""}")
                }, text = user.phone, modifier = Modifier.align(TopStart))
                OpenView(intent = sendToIntent.apply {
                    data = Uri.parse("mailto:${user.email ?: ""}")
                }, text = user.email, modifier = Modifier.align(TopEnd))
            }

            ExpandableContent(expand, expand) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Username: ${user.username}",
                        modifier = Modifier
                            .padding(8.dp, 12.dp)
                            .align(TopStart)
                    )
                    user.website?.let {
                        OpenView(intent = viewIntent.apply {
                            data = Uri.parse("http://${it}")
                        }, text = it, modifier = Modifier.align(TopEnd))
                    }
                }
                user.company?.let {
                    CompanyView(company = user.company)
                }
                user.address?.let {
                    AddressView(
                        address = user.address,
                        Modifier
                            .padding(2.dp)
                            .align(CenterHorizontally),
                        onClick = {
                            user.address.geo?.let {
                                mapClick(it)
                            }
                        })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressPreview() {
    AppTheme {
        val address = Address("47th Street", null, "New York", "10101", Location(34.5, 57.8))

        AddressView(address, Modifier
            .padding(2.dp), { })
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    AppTheme {
        val address = Address("47th Street", null, "New York", "10101", Location(34.5, 57.8))
        val company = Company("Kramerica", "We put the oil in the ball", "YEAH")
        val user = User(
            1,
            "Johnny Smith",
            "jsmith",
            "johnny@smith.com",
            address,
            "+187547773748",
            "www.jsmith.com",
            company
        )
        UserView(user, { })
    }
}