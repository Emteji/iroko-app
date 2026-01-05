package com.emteji.lifepathchild.app.parent.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.parent.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ParentOnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: ParentOnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 5 })
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(IrokoCream)
        ) {
            // Background Layer (Parchment Texture)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ParchmentGradient)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { page ->
                OnboardingPageContentNew(page = page)
            }

            // --- BOTTOM CARD / CONTROLS (Floating over the immersive background) ---
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f) // Taller card for better text fit
            ) {
                // Organic Soil Shape
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path().apply {
                        // Start slightly below top-left to create wave
                        moveTo(0f, 80f)
                        // Organic wave
                        cubicTo(
                            size.width * 0.3f, 0f,
                            size.width * 0.7f, 120f,
                            size.width, 60f
                        )
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    
                    // Gradient fill (Dark Soil)
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(IrokoBrown, Color(0xFF3E2723), Color.Black),
                            startY = 0f,
                            endY = size.height
                        )
                    )
                    
                    // Top gold edge accent
                    drawPath(
                        path = path,
                        style = Stroke(width = 4f),
                        brush = Brush.horizontalGradient(
                            colors = listOf(IrokoGold.copy(alpha = 0.3f), IrokoGold, IrokoGold.copy(alpha = 0.3f))
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp, start = 32.dp, end = 32.dp, bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Page Indicator
                    Row(
                        modifier = Modifier.padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { iteration ->
                            val color = if (pagerState.currentPage == iteration) IrokoGold else Color.White.copy(alpha = 0.2f)
                            val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color)
                                    .size(width = width, height = 8.dp)
                            )
                        }
                    }

                    // Dynamic Text Content based on page
                    val (title, body) = when (pagerState.currentPage) {
                        0 -> "You Are Not Raising a Child" to "You are shaping a life that will stand in the world long after you."
                        1 -> "Every Strong Life Has Roots" to "IROKO helps you build discipline, values, and confidence through daily life training."
                        2 -> "No Child Grows Alone" to "IROKO recreates the village. Guidance. Structure. Accountability. All shaped by your values."
                        3 -> "You Are the Guardian" to "You decide the rules. You approve rewards. IROKO works for you."
                        4 -> "Welcome to IROKO" to "Let us begin with your child."
                        else -> "" to ""
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = IrokoGold,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = body,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Main Action Button
                    val buttonText = when (pagerState.currentPage) {
                        0 -> "Begin the Journey"
                        4 -> "Set Up Child Profile"
                        else -> "Continue"
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage < 4) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                viewModel.completeOnboarding()
                                onOnboardingComplete()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(IrokoGold, IrokoBurntGold)
                                ),
                                RoundedCornerShape(12.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = buttonText.uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = IrokoBrown,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingPageContentNew(page: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Full Screen Procedural Backgrounds
        when (page) {
            0 -> BackgroundScenePurpose()
            1 -> BackgroundSceneRoots()
            2 -> BackgroundSceneVillage()
            3 -> BackgroundSceneGuardian()
            4 -> BackgroundSceneTransition()
        }
        
        // Logo at top
        Text(
            text = "IROKO",
            style = MaterialTheme.typography.displayMedium,
            color = IrokoBrown,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp)
        )
    }
}